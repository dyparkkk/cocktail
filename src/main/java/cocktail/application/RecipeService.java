package cocktail.application;

import cocktail.domain.Order;
import cocktail.domain.Recipe;
import cocktail.domain.Tag;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import cocktail.infra.RecipeRepository;
import cocktail.infra.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static cocktail.dto.RecipeRequestDto.*;
import static cocktail.dto.RecipeResponseDto.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;

    /**
     * 회원 정보 추가
     * Brewing, Base 저장 추가
     */
    @Transactional
    public Long createRecipe(RecipeRequestDto dto) {
        // Recipe 생성
        BigDecimal decimalDosu = new BigDecimal(dto.getDosu());
        List<Order> orderList = dto.getOrders().stream()
                .map(OrderDto::toOrder).collect(Collectors.toList());

        Recipe recipe = Recipe.builder()
                .name(dto.getName())
                .dosu(decimalDosu)
                .orders(orderList).build();
        recipeRepository.save(recipe);

        // dto로 부터 tag 생성해서 저장
        List<String> stringTags = dto.getTags();
        List<Tag> tagList = stringTags.stream().map(s -> new Tag(s, recipe))
                .collect(Collectors.toList());
        tagRepository.saveAll(tagList);

        return recipe.getId();
    }

    @Transactional
    public List<RecipeListDto> findAllPageable(Pageable pageable){
        return recipeRepository.findAllListDto(pageable);
    }

    @Transactional
    public List<RecipeResponseDto> filterSearch(SearchCondition condition, Pageable pageable){
        return recipeRepository.filterSearch(condition, pageable);
    }

}