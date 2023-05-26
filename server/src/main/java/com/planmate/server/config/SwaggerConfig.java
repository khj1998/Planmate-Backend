package com.planmate.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;


/**
 * @author 지승언
 * swagger-ui 설정 파일
 * */
@Configuration
public class SwaggerConfig {
    private static final String REFERENCE = "Bearer ";

    /**
     * controller만 표시하도록 설정
     * */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(SwaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.planmate.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(bearerAuthSecurityScheme()));
    }

    private ApiInfo SwaggerInfo() {
        return new ApiInfoBuilder()
                .title("planmate Swagger")
                .description("메인 API Swagger 문서")
                .version("1.0")
                .build();
    }

    /**
     * security 적용 시 sweager도 security 적용
     * */
    private SecurityContext securityContext() {
        return springfox.documentation
                .spi.service.contexts
                .SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext -> true)
                .build();
    }

    /**
     * authorization 버튼 설정
     * */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference(REFERENCE, authorizationScopes));
    }

    private HttpAuthenticationScheme bearerAuthSecurityScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name(REFERENCE)
                .build();
    }
}
