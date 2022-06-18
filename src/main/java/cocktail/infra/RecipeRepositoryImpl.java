package cocktail.infra;

import cocktail.domain.QRecipe;
import cocktail.domain.Recipe;
import cocktail.dto.QRecipeResponseDto_RecipeListDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cocktail.domain.QRecipe.*;
import static cocktail.dto.RecipeResponseDto.*;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
}
