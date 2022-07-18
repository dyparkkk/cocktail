package cocktail.infra.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

@Import(S3MockConfig.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class S3Test {

    @Autowired
    AmazonS3 amazonS3;
    @Autowired
    S3Mock s3Mock;

    private static final String BUCKET_NAME = "testbucket";

    @BeforeEach
    void before() {
        s3Mock.start();
        amazonS3.createBucket(BUCKET_NAME);
    }

    @AfterEach
    void after() {
        amazonS3.shutdown();
        s3Mock.stop();
    }

    @Test
    @DisplayName("s3 import 테스트")
    void S3Import() throws IOException {
        // given
        // https://galid1.tistory.com/591 - ObjectMetaData를 사용한 쓰기 없는 s3 테스트
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_PNG_VALUE);
        String path = "test/test.png";
        PutObjectRequest putObjectReq =
                new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)), metadata);
        amazonS3.putObject(putObjectReq);

        //when
        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, path);

        //then
        assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(MediaType.IMAGE_PNG_VALUE);
        assertThat(new String(FileCopyUtils.copyToByteArray(s3Object.getObjectContent()))).isEqualTo("");
    }
}