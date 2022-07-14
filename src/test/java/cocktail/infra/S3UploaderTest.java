package cocktail.infra;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class S3UploaderTest {

    private MockMultipartFile multipartFile1;

    @BeforeEach
    void before() throws IOException {
        String strUrl = "https://cocktail-image.s3.ap-northeast-2.amazonaws.com/testFile.png";
        URL url = new URL(strUrl);
        String extension = strUrl.substring(strUrl.lastIndexOf(".")+1); // 확장자
        String fileName = "testFile"; // 이미지 이름

        // url to file
        BufferedImage image = ImageIO.read(url);
        File file = new File(fileName + "." + extension);
        ImageIO.write(image, extension, file);

        multipartFile1 = new MockMultipartFile("data",
                "testFile.png", "image/png",
                new FileInputStream(file));
    }

    @AfterEach
    void after() {

    }

    @Test
    void test2() {



    }

}