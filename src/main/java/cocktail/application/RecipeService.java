package cocktail.application;

import cocktail.domain.recipe.Ingredient;
import cocktail.domain.recipe.Order;
import cocktail.domain.recipe.Recipe;
import cocktail.domain.recipe.Tag;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import cocktail.infra.recipe.IngredientRepository;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.recipe.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cocktail.dto.RecipeRequestDto.*;
import static cocktail.dto.RecipeResponseDto.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * 회원 정보 추가
     * Brewing, Base 저장 추가
     */
    @Transactional
    public Long createRecipe(RecipeRequestDto dto) {
        // Recipe 생성
        List<Order> orderList = dtosToOrders(dto.getOrders());

        Recipe recipe = Recipe.builder()
                .name(dto.getName())
                .dosu(dto.getDosu())
                .brewing(dto.getBrewing())
                .base(dto.getBase())
                .orders(orderList).build();
        recipeRepository.save(recipe);

        // dto로 부터 tag 생성해서 저장
        List<Tag> tagList = dtosToTags(dto.getTags(), recipe);
        tagRepository.saveAll(tagList);

        // ingredient 생성해서 저장
        List<Ingredient> ingredients = dtoToIngredients(dto.getIngredients(), recipe);
        ingredientRepository.saveAll(ingredients);

        return recipe.getId();
    }

    @Transactional
    public List<RecipeResponseDto> findAllPageable(Pageable pageable){
        return recipeRepository.findAllListDto(pageable);
    }

    @Transactional
    public List<RecipeResponseDto> filterSearch(SearchCondition condition, Pageable pageable){
        return recipeRepository.filterSearch(condition, pageable);
    }

    @Transactional
    public Long update(Long id, RecipeRequestDto dto){
        Recipe recipe = recipeRepository.fetchFindById(id)
                .orElseThrow(() -> new IllegalArgumentException("RecipeService.update : id값을 찾을 수 없습니다."));

        // 값 바꿔주기
        List<Order> orders = dtosToOrders(dto.getOrders());
        recipe.update(dto.getName(), dto.getDosu(), dto.getBrewing(), dto.getBase(), orders);

        // 전에 있던 태그 삭제 후 저장
        recipeRepository.deleteTags(id);
        List<Tag> tagList = dtosToTags(dto.getTags(), recipe);
        tagRepository.saveAll(tagList);

        // 전에 있던 재료 삭제 후 저장
        recipeRepository.deleteIngredients(id);
        List<Ingredient> ingredientList = dtoToIngredients(dto.getIngredients(), recipe);
        ingredientRepository.saveAll(ingredientList);

        return id;
    }

    @Transactional
    public DetailDto findById(Long id) {
        Recipe recipe = recipeRepository.fetchFindById(id)
                .orElseThrow(() -> new IllegalArgumentException("RecipeService.findById : id값을 찾을 수 없습니다."));

        return DetailDto.from(recipe);
    }

    private List<Ingredient> dtoToIngredients(List<IngredientDto> dtos, Recipe recipe) {
        return dtos.stream()
                .map(ingredientDto -> ingredientDto.toEntity(recipe))
                .collect(toList());
    }

    private List<Tag> dtosToTags(List<String> stringTags, Recipe recipe) {
        return stringTags.stream()
                .map(s -> new Tag(s, recipe))
                .collect(toList());
    }

    private List<Order> dtosToOrders(List<OrderDto> orderDtos) {
        return orderDtos.stream()
                .map(OrderDto::toOrder).collect(toList());
    }
}
