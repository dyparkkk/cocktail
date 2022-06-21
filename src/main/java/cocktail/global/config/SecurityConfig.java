package cocktail.global.config;

import cocktail.application.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    // 참고 : https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                .cors().and().csrf().disable() //h2-console 화면을 사용하기 위해 해당 옵션을 disable.
                .authorizeRequests() //URL 별 권한 관리를 설정하는 옵션의 시작점. 이 부분이 선언되어야 antMatchers 옵션을 사용할 수 있다.
                .anyRequest().permitAll() //설정된 값들 이외 나머지 URL authenticated()를 추가해 나머지 URL들은 모두 인증된 사용자만 허용한다.(로그인 한 사용자)
                .and().oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);// 코드를 받지 않고 액세스 토큰 + 사용자 프로필 정보를 한번에 받음.


             return http.build();
    }

    // encoder를 빈으로 등록.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
