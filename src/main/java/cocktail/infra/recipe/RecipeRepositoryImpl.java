package cocktail.infra.recipe;

import cocktail.domain.recipe.*;

import cocktail.domain.user.QUser;
import cocktail.domain.user.User;
import cocktail.dto.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cocktail.domain.recipe.QIngredient.*;
import static cocktail.domain.recipe.QOrder.*;
import static cocktail.domain.recipe.QRecipe.*;
import static cocktail.domain.recipe.QTag.*;
import static cocktail.domain.user.QUser.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Recipe> findAllRecipe(Pageable pageable) {
        return queryFactory
                .selectFrom(recipe)
                .distinct()
                .leftJoin(recipe.user, user)
                .leftJoin(recipe.tags, tag).fetchJoin()
                .offset(pageable.getOffset()) // 몇번째부터 시작
                .limit(pageable.getPageSize()) // 한 페이지에 몇개 가져옴
                .orderBy(
                        starSort(pageable).stream()
                                .toArray(OrderSpecifier[]::new))
                .fetch();
    }

    @Override
    public List<Recipe> filterSearch(SearchCondition condition, Pageable pageable) {
        return queryFactory
                .selectFrom(recipe)
                .distinct()
                .leftJoin(recipe.tags, tag).fetchJoin()
                .leftJoin(recipe.ingredients, ingredient)
                .where(
                        tagEq(condition.getTagList()),
                        brewingEq(condition.getBrewing()),
                        baseEq(condition.getBase()),
                        nameContains(condition.getName()),
                        dosuBetween(condition.getLeastDosu(), condition.getMaxDosu()),
                        ingredientEq(condition.getIngredientList())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
                        starSort(pageable).stream()
                                .toArray(OrderSpecifier[]::new))
                .fetch();
    }

    @Override
    public long deleteTags(Long id) {
        return queryFactory
                .delete(tag)
                .where(tag.recipe.id.eq(id))
                .execute();
    }

    @Override
    public Optional<Recipe> fetchFindById(Long id) {
        Recipe result = queryFactory
                .selectFrom(recipe)
                .distinct()
                .innerJoin(recipe.tags, tag)
                .innerJoin(recipe.ingredients, ingredient).fetchJoin()
                .join(recipe.orders, order)
                .where(recipe.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public long deleteIngredients(Long id) {
        return queryFactory
                .delete(ingredient)
                .where(ingredient.recipe.id.eq(id))
                .execute();
    }

    @Override
    public long viewCntPlus(Long id) {
        return queryFactory
                .update(recipe)
                .set(recipe.viewCnt, recipe.viewCnt.add(1))
                .where(recipe.id.eq(id))
                .execute();
    }

    @Override
    public List<Recipe> findAllByUser(User user) {
        return queryFactory
                .selectFrom(recipe)
                .where(recipe.user.eq(user))
                .orderBy(recipe.createdDate.desc())
                .fetch();
    }

    private List<OrderSpecifier> starSort(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();
        if(! pageable.getSort().isEmpty()) {
            for(Sort.Order order : pageable.getSort()){
//            Order dir = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                if(order.getProperty().equals("star")){
                    OrderSpecifier<?> orderStar = getSortedColumn(Order.DESC, recipe, "star");
                    orders.add(orderStar);
                }
            }
        }

        OrderSpecifier<?> orderCreatedDate = getSortedColumn(Order.DESC, recipe, "createdDate");
        orders.add(orderCreatedDate);
        return orders;
    }

    private OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    private BooleanBuilder tagEq(List<String> tagList){
        if( tagList == null) return null;

        BooleanBuilder builder = new BooleanBuilder();
        for(String i : tagList){
            if(hasText(i)){
                builder.and(tag.name.eq(i));
            }
        }

        return builder;
    }
    private BooleanBuilder ingredientEq(List<String> ingredientList) {
        if(ingredientList == null) return null;

        BooleanBuilder builder = new BooleanBuilder();
        for(String i : ingredientList){
            if(hasText(i)){
                builder.and(ingredient.name.contains(i));
            }
        }
        return builder;
    }

    private BooleanExpression brewingEq(Brewing brewing){
        return brewing != null ? recipe.brewing.eq(brewing) : null;
    }

    private BooleanExpression baseEq(Base base) {
        return base != null ? recipe.base.eq(base) : null;
    }

    private BooleanExpression nameContains(String name){
        return hasText(name) ? recipe.name.contains(name) : null;
    }

    private BooleanBuilder dosuBetween(BigDecimal leastDosu, BigDecimal maxDosu) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(dosuMax(maxDosu)).and(dosuMin(leastDosu));
        return builder;
    }

    private BooleanExpression dosuMax(BigDecimal maxDosu) {
        return maxDosu != null ? recipe.dosu.loe(maxDosu) : null;
    }
    private BooleanExpression dosuMin(BigDecimal leastDosu) {
        return leastDosu != null ? recipe.dosu.goe(leastDosu) : null;
    }

}
