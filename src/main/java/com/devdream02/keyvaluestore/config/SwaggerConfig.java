package com.devdream02.keyvaluestore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${devdream02.base_uri}")
    private String baseUri;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).host(baseUri).select()
                .apis(RequestHandlerSelectors.basePackage("com.devdream02.keyvaluestore.controller"))
                .paths(PathSelectors.regex("/.*")).build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Keyvalue store Service")
                .description("Key value store service")
                .contact(new Contact("Devyansh Mittal", "www.github.com/devdream02", "devdream02@gmail.com"))
                .license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").version("1.0")
                .build();
    }
}