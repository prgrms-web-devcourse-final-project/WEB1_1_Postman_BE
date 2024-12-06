package postman.bottler.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public OpenAPI openAPI() {
//        String jwt = "JWT";
//
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
//
//        Components components = new Components()
//                .addSecuritySchemes(jwt, new SecurityScheme()
//                        .name(jwt)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT")
//                );
//
//        Server server = new Server()
//                .url("https://api.bottler.store")
//                .description("Bottler API 서버");
//
//        return new OpenAPI()
//                .info(apiInfo())
//                .addServersItem(server)
//                .components(components)
//                .addSecurityItem(securityRequirement);
//    }
//
//    private Info apiInfo() {
//        return new Info()
//                .title("Bottler API")
//                .description("Bottler API 문서")
//                .version("1.0.0");
//    }
//}

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    private Info apiInfo() {
        return new Info()
                .title("Bottler API") // API의 제목
                .description("Bottler API 문서") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}

