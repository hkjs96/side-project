package com.sideproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfiguration {

    // TODO: 참고 - https://devfunny.tistory.com/692

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Side Project API")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sideproject"))
                .paths(PathSelectors.any())
                .build()
                .tags(
                        new Tag("user-controller", "User API")
//                        , new Tag( ~~~ )
                );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("사이드 프로젝트 - Swagger")
                .description("사이드 프로젝트 - 대학생 협업 프로그램 API")
                .version("1.0.0")
                .build();
    }
}
