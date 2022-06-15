package cocktail.application;

import cocktail.domain.Order;
import cocktail.domain.Recipe;
import cocktail.domain.Tag;
import cocktail.dto.RecipeRequestDto;
import cocktail.infra.RecipeRepository;
import cocktail.infra.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static cocktail.dto.RecipeRequestDto.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Long createRecipe(RecipeRequestDto dto) {
        // Recipe 생성
        BigDecimal decimalDosu = new BigDecimal(dto.getDosu());
        List<Order> orderList = dto.getOrders().stream()
                .map(OrderDto::toOrder).collect(Collectors.toList());

        Recipe recipe = new Recipe(dto.getName(), decimalDosu, orderList);
        recipeRepository.save(recipe);

        // dto로 부터 tag 생성해서 저장
        List<String> stringTags = dto.getTags();
        List<Tag> tagList = stringTags.stream().map(s -> new Tag(s, recipe))
                .collect(Collectors.toList());
        tagRepository.saveAll(tagList);

        return recipe.getId();
    }

}
