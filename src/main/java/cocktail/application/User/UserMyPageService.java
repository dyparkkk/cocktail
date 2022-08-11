package cocktail.application.User;

import cocktail.application.auth.SessionUser;
import cocktail.domain.user.Bookmark;
import cocktail.domain.user.Follow;
import cocktail.domain.user.User;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.BookmarkDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.UserPageDto;
import cocktail.infra.user.BookmarkRepository;
import cocktail.infra.user.FollowRepository;
import cocktail.infra.user.UserRepository;
import cocktail.infra.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class UserMyPageService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FollowRepository followRepository;
    /**
     * user A의 정보를 보여준다.
     * (레시피 수, 팔로잉 수, 팔로워 수, url, text, 내가 팔로우 중인지 아닌지)
     */
    @Transactional
    public UserPageDto getUserPage(String username, SessionUser sessionUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(IllegalStateException::new);

        UserPageDto res = UserPageDto.from(user);
        if(sessionUser != null){
            User myUser = userRepository.findByUsername(sessionUser.getUsername())
                    .orElseThrow(IllegalStateException::new);
            List<Follow> followingList = followRepository.findFollowingList(myUser);
            for (Follow follow : followingList) {
                if (follow.getToUser().getId().equals(user.getId())) {
                    res.setMyFollow(true);
                }
            }
        }
        return res;
    }

    @Transactional
    public List<RecipeResponseDto> getUserRecipes(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(IllegalStateException::new);

        List<Recipe> recipes = recipeRepository.findAllByUser(user);
        return recipes.stream()
                .map(RecipeResponseDto::fromEntity)
                .collect(toList());
    }

    @Transactional
    public void createBookmark(BookmarkDto dto){
        String username = dto.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(IllegalArgumentException::new);

        Recipe recipe = recipeRepository.findById(dto.getRecipeId())
                .orElseThrow(IllegalArgumentException::new);

        // 이미 북마크인지 확인
        bookmarkRepository.findOptional(user, recipe)
                .ifPresent(b -> {
                    throw new IllegalStateException("이미 북마크 되어있음");
                });

        // 북마크 추가
        bookmarkRepository.save(new Bookmark(user, recipe));
    }




}
