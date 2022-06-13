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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;

    @Transactional
    public void createRecipe(RecipeRequestDto dto) {
        // Recipe 생성
        BigDecimal decimalDosu = stringToBigDecimal(dto.getDosu());

        Recipe recipe = new Recipe(dto.getName(), decimalDosu, dto.getOrders());
        recipeRepository.save(recipe);

        // dto로 부터 tag 생성해서 저장
        List<String> stringTags = dto.getTags();
        List<Tag> tagList = stringTags.stream().map(s -> new Tag(s, recipe))
                .collect(Collectors.toList());
        saveTagList(tagList);

        //
    }

    private BigDecimal stringToBigDecimal(String dosu) {
        return BigDecimal.valueOf(Double.valueOf(dosu));
    }

    private void saveTagList(List<Tag> tagList) {
        tagList.stream().forEach(tag -> tagRepository.save(tag));
    }
}
