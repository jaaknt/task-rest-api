package com.multimarketing.taskrestapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
/*
@OpenAPIDefinition(
        info = @Info(
                title = "Code-First Approach",
                description = "Spring Doc Sample",
                contact = @Contact(name = "Hendi Santika", url = "https://s.id/hendisantika", email =
                        "hendisantika@yahoo.co.id"),
                license = @License(name = "MIT Licence", url = "https://github.com/hendisantika")),
        servers = @Server(url = "http://localhost:8080")
)
//@SecurityScheme(name = "api", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SpringDocConfig {
}
*/


@Configuration
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("spring")
                .pathsToMatch("/**")
                .build();
    }
}


