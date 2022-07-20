package cocktail.api;

import cocktail.infra.S3Uploader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recipe")
@Api(tags = "recipe_Function")
public class RecipeFucApi {

    private final S3Uploader s3Uploader;

    public void giveStarApi() {

    }

    @PostMapping("/upload")
    @ApiOperation(value = "upload image", notes = "복수 일 경우 여러번 호출해야 함")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile);
    }
}
