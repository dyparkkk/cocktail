package cocktail.global.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //localhost:8080/swagger-ui.html  스웨거 주소
    // 참고 : https://velog.io/@mbsik6082/Spring-Boot-2.6.2에-Swagger-적용-시-오류

    //스웨거 페이지에 소개될 설명들
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Cocktail REST API")
                .version("1.0.0")
                .description("칵테일 프로젝트 swagger api 입니다.")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Cocktail swagger")//빈설정을 여러개 해줄경우 구분하기 위한 구분자.
                .apiInfo(this.apiInfo())//스웨거 설명
                .select()//apis, paths를 사용하주기 위한 builder
                .apis(RequestHandlerSelectors.any())//현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.any())//스웨거에서 보여줄 api 필터
                .build()
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));

    }


    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("apiKey",
                authorizationScopes));
    }

    //swagger ui 설정.
    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true)
                .validatorUrl("")
                .build();
    }

    /**
     * * 추가적으로 @WebMvcConfigurationSupport 에 대해 적어보자면..
     *
     * Spring Boot 의 경우 @EnableWebMvc 을 명시하게 되면 WebMvcConfigurationSupport 가 자동으로 빈으로
     * 등록되기에 Spring boot Web 기본 설정을 잡아주는 WebMvcAutoConfiguration 이 동작하지 않는다고 한다.
     *
     * 이유는 이 WebMvcAutoConfiguration 에 @ConditionalOnMissingBean(WebMvcConfigurationSupport.class) 라고
     * 명시되어 있기 때문이라고 한다.
     * (WebMvcConfigurationSupport 클래스가 Bean 으로 등록되어있지 않을 경우에만 WebMvcAutoConfiguration 가 동작하라는 뜻이라고 한다.
     *
     * WebMvcAutoConfiguration 이 하는 Spring boot Web 기본 설정은 Spring MVC 에서의 <mvc:annotation-driven /> 과 같다고 한다.
     * (RequestMappingHandler,RequestMappingHandlerAdapter,ExceptionHandlerExceptionResolver 등 Web 에 필요한 Bean 들을 자동으로 설정하주는..)
     * 그렇기에 Spring boot Web 의 기본 설정이 필요없고 커스텀한 설정을 하고 싶을 때에만 @EnableWebMvc 를 명시하고
     * 그렇지 않을 경우에는 자동설정 + WebMvcConfigurerAdapter 를 사용하면 된다고 한다.
     */
}
