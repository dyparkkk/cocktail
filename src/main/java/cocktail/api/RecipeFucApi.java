package cocktail.api;

import cocktail.infra.S3Uploader;
import io.swagger.annotations.Api;
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
@Api(tags = "star")
public class RecipeFucApi {

    private final S3Uploader s3Uploader;

    public void giveStarApi() {

    }

    @PostMapping("/upload")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        System.out.println("contentType = " + multipartFile.getContentType());
        System.out.println("name = " + multipartFile.getName());
        return s3Uploader.upload(multipartFile);
    }
}
