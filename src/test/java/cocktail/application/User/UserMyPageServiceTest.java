package cocktail.application.User;

import cocktail.domain.user.User;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.BookmarkDto;
import cocktail.infra.user.UserRepository;
import cocktail.infra.recipe.RecipeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserMyPageServiceTest {

    @InjectMocks
    private UserMyPageService userMyPageService;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("북마크 생성이 성공적으로 된다.")
    void createBookmarkTest(){

    }

    @Test
    @DisplayName("user를 찾지 못할 경우 IllegalArgumentException을 반환한다. ")
    void createBookmark_findByUsernameTest(){
        // given
        String username = "user1";
        Long recipeId = 1L;

        Recipe recipe = Recipe.builder().build();
        BookmarkDto dto = new BookmarkDto(username, recipeId);

        // when
        given(userRepository.findByUsername(username))
                .willReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> userMyPageService.createBookmark(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("recipe를 찾지 못했을 경우 IllegalArgumentException을 반환한다. ")
    void createBookmark_findByIdTest(){
        // given
        String username = "user1";
        Long recipeId = 123L;

        User user = User.builder().username(username).build();
        BookmarkDto dto = new BookmarkDto(username, recipeId);

        given(userRepository.findByUsername(username))
                .willReturn(Optional.ofNullable(user));
        // when
        given(recipeRepository.findById(recipeId))
                .willReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> userMyPageService.createBookmark(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }
}