package cocktail.infra;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@TestConfiguration
public class S3MockConfig {

    /**
     * 참고 :
     * https://wedul.site/704
     * https://blog.junu.dev/27
     */

}
