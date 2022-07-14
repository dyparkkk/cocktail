package cocktail.application.recipe;

import cocktail.application.auth.SessionUser;
import cocktail.domain.User;
import cocktail.domain.recipe.Ingredient;
import cocktail.domain.recipe.Order;
import cocktail.domain.recipe.Recipe;
import cocktail.domain.recipe.Tag;
import cocktail.dto.RecipeRequestDto;
import cocktail.infra.recipe.IngredientRepository;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.recipe.TagRepository;
import cocktail.infra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cocktail.dto.RecipeRequestDto.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createRecipe(RecipeRequestDto dto, SessionUser sessionUser) {
        // Recipe 생성
        Recipe recipe = dtoToRecipe(dto);
        recipeRepository.save(recipe);

        // dto로 부터 tag 생성해서 저장
        if(dto.getTags() != null){
            List<Tag> tagList = dtosToTags(dto.getTags(), recipe);
            tagRepository.saveAll(tagList);
        }

        // ingredient 생성해서 저장
        List<Ingredient> ingredients = dtoToIngredients(dto.getIngredients(), recipe);
        ingredientRepository.saveAll(ingredients);

        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(IllegalArgumentException::new);
        recipe.setUser(user);

        return recipe.getId();
    }


    @Transactional
    public Long update(Long id, RecipeRequestDto dto, SessionUser user){
        Recipe recipe = recipeRepository.fetchFindById(id)
                .orElseThrow(IllegalArgumentException::new);

        // 유저와 레시피 작성자가 맞는지 확인
        isValid(user, recipe);

        // 값 바꿔주기
        List<Order> orders = dtosToOrders(dto.getOrders());
        recipe.update(dto.getName(), dto.getDosu(), dto.getBrewing(), dto.getBase(), dto.getGarnish(),
                dto.getGlass(), dto.getSoft(), dto.getSweet(), orders);

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
    public void deleteRecipe(Long id, SessionUser user) {
        Recipe recipe = recipeRepository.fetchFindById(id)
                .orElseThrow(IllegalArgumentException::new);

        // 유저와 레시피 작성자가 맞는지 확인
        isValid(user, recipe);

        // 레시피 삭제
        recipeRepository.delete(recipe);
    }

    private Recipe dtoToRecipe(RecipeRequestDto dto) {
        List<Order> orderList = dtosToOrders(dto.getOrders());

        Recipe recipe = Recipe.builder()
                .name(dto.getName())
                .dosu(dto.getDosu())
                .brewing(dto.getBrewing())
                .base(dto.getBase())
                .orders(orderList)
                .garnish(dto.getGarnish())
                .glass(dto.getGlass())
                .sweet(dto.getSweet())
                .soft(dto.getSoft())
                .build();
        return recipe;
    }

    private void isValid(SessionUser user, Recipe recipe) {
        if (!recipe.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("작성자가 아님");
        }
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
