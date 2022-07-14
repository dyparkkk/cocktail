package cocktail.api;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recipe")
@Api(tags = "star")
public class RecipeFucApi {

    public void giveStarApi() {

    }
}
