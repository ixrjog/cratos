package com.baiyi.cratos.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author baiyi
 * @Date 2024/1/3 14:15
 * @Version 1.0
 */
@Slf4j
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@Configuration
public class OpenAPIConfiguration {

    @Value("${spring.application.name}")
    private String name;

    @Value("${spring.application.version}")
    private String version;

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Bean
    public OpenAPI restfulOpenAPI() {
        License license = new License().name("APACHE LICENSE, VERSION 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        Contact contact = new Contact()
                .url("https://github.com/ixrjog/cratos")
                .name("Jan & Xiu")
                .email("");

        return new OpenAPI()
                .info(new Info()
                        .title(name.toUpperCase())
                        .version(version)
                        .description("The Open API")
                        .contact(contact)
                        .license(license));
    }

}