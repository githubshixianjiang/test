package com.gxzd.gxzd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        //参数构造器
        ParameterBuilder tokenValue = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        //header中的token参数非必填，传空也可以
        tokenValue.name("token").description("用户令牌")
                .modelRef(new ModelRef("String")).parameterType("header")
                .required(false).build();
        //根据每个方法名也知道当前方法在设置设什么参数
        pars.add(tokenValue.build());
        return new Docket(DocumentationType.SWAGGER_2).
                useDefaultResponseMessages(false)
                .groupName("web.api")
                .apiInfo(webApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gxzd.gxzd.controller"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .globalOperationParameters(pars);

    }
    private List<ApiKey> securitySchemes() {
        List<ApiKey> objects = new ArrayList<>();
        objects.add(new ApiKey("token", "token", "header"));
        return objects;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> objects = new ArrayList<>();
        SecurityContext build = SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("^(?!auth).*$")).build();
        objects.add(build);
        return objects;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        SecurityReference authorization = new SecurityReference("token", authorizationScopes);
        List<SecurityReference> arrayList = new ArrayList();
        arrayList.add(authorization);
        return arrayList;
    }

    private ApiInfo webApiInfo() {

        return new ApiInfoBuilder()
                .title("共享账单")
                .description("本文档描述了“共享账单”接口定义")
                .version("1.0")
                .contact(new Contact("java","",""))
                .build();
    }

}
