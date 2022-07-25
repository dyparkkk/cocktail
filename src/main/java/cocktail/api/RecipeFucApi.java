package cocktail.api;

import cocktail.application.auth.SessionUser;
import cocktail.application.recipe.StarService;
import cocktail.dto.StarDto;
import cocktail.global.config.Login;
import cocktail.infra.S3Uploader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recipe")
@Api(tags = "recipe_Function")
public class RecipeFucApi {

    private final S3Uploader s3Uploader;
    private final StarService starService;

    @PostMapping("/star/{id}")
    @ApiOperation(value = "score recipe with stars")
    public ResponseEntity<String> giveStarApi(@Valid @PathVariable Long id,
                                              @RequestBody StarDto starDto,
                                              @ApiIgnore @Login SessionUser sessionUser) {
        starService.giveStar(starDto, id, sessionUser);
        return new ResponseEntity<>("score recipe with stars", HttpStatus.ACCEPTED);
    }

    @PostMapping("/upload")
    @ApiOperation(value = "upload image", notes = "복수 일 경우 여러번 호출해야 함")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile);
    }
}
