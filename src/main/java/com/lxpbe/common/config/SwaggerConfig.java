package com.lxpbe.common.config;

import com.lxpbe.common.exception.ErrorBody;
import com.lxpbe.common.security.LoginUser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfig {

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(LoginUser.class);
    }

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "cookieAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Learning Crew API")
                        .description("Learning Crew API 문서")
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name("access_token")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .description("JWT 인증 쿠키")));
    }

    @Bean
    public GlobalOpenApiCustomizer globalResponseCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperations().forEach(operation -> {

                    boolean isAuthApi = path.startsWith("/auth/");
                    boolean isPublicGet = path.startsWith("/courses/")
                            && operation.getOperationId() != null
                            && (operation.getOperationId().contains("search")
                            || operation.getOperationId().contains("getCourse"));

                    if (!isAuthApi && !isPublicGet) {

                        operation.getResponses().computeIfAbsent("401", k ->
                                new ApiResponse()
                                        .description("인증 실패")
                                        .content(new Content().addMediaType(
                                                        "application/json",
                                                        new MediaType()
                                                                .examples(Map.of(
                                                                        "로그인 필요", new Example().value(
                                                                                new com.lxpbe.common.response.ApiResponse<>(
                                                                                        null,
                                                                                        new ErrorBody("AUTH_004", "로그인이 필요합니다.")
                                                                                )
                                                                        ),
                                                                        "토큰 만료", new Example().value(
                                                                                new com.lxpbe.common.response.ApiResponse<>(
                                                                                        null,
                                                                                        new ErrorBody("AUTH_003", "만료된 토큰입니다.")
                                                                                )
                                                                        ),
                                                                        "유효하지 않은 토큰", new Example().value(
                                                                                new com.lxpbe.common.response.ApiResponse<>(
                                                                                        null,
                                                                                        new ErrorBody("AUTH_003",
                                                                                                "유효하지 않은 토큰입니다.")
                                                                                )
                                                                        )
                                                                ))
                                                )
                                        ));
                    }
                })
        );
    }
}
