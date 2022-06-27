package cocktail.application.User;

import cocktail.domain.Bookmark;
import cocktail.domain.User;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.BookmarkDto;
import cocktail.infra.UserRepository;
import cocktail.infra.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public void createBookmark(BookmarkDto dto){
        String username = dto.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(IllegalArgumentException::new);

        Recipe recipe = recipeRepository.findById(dto.getRecipeId())
                .orElseThrow(IllegalArgumentException::new);

        // 이미 북마크인지 확인

        // 북마크 추가
    }
}
