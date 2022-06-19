package cocktail.infra;

import cocktail.domain.*;
import cocktail.dto.QRecipeResponseDto;
import cocktail.dto.QRecipeResponseDto_RecipeListDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

import static cocktail.domain.QRecipe.*;
import static cocktail.domain.QTag.*;
import static cocktail.dto.RecipeResponseDto.*;
import static org.springframework.util.StringUtils.*;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final int TAG_LIMIT_NUM = 3;

    @Override
    public List<RecipeListDto> findAllListDto(Pageable pageable) {
        return queryFactory
                .select(new QRecipeResponseDto_RecipeListDto(
                        recipe.name
                ))
                .from(recipe)
                .offset(pageable.getOffset()) // 몇번째부터 시작
                .limit(pageable.getPageSize()) // 한 페이지에 몇개 가져옴
                .fetch();
    }

    @Override
    public List<RecipeResponseDto> filterSearch(SearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(new QRecipeResponseDto(
                        recipe.id,
                        recipe.name
                ))
                .from(tag)
                .join(tag.recipe, recipe)
                .where(
                        tagEq(condition.getTagList()),
                        brewingEq(condition.getBrewing()),
                        baseEq(condition.getBase()),
                        nameContains(condition.getName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanBuilder tagEq(List<String> tagList){
        if( tagList == null) return null;

        BooleanBuilder builder = new BooleanBuilder();
        Iterator<String> iterator = tagList.iterator();
        int cnt = 0;
        while(iterator.hasNext() && cnt < TAG_LIMIT_NUM){
            builder.and(tag.name.eq(iterator.next()));
            cnt++;
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

}
