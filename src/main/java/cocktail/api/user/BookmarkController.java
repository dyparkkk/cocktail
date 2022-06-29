package cocktail.api.user;

import cocktail.application.User.BookmarkService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/bookmark")
@Api(tags = "bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final HttpSession httpSession;

//    @GetMapping("/")
//    public ResponseEntity<String> createBookmark() {
//        return null;
//    }

}
