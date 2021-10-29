package com.dzindo.jwt;

import com.dzindo.jwt.domain.AppUser;
import com.dzindo.jwt.domain.Role;
import com.dzindo.jwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

import java.util.ArrayList;

@SpringBootApplication
@EnableSwagger2
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new AppUser(null, "Nedzad Dzindo", "ndzindo", "password", new ArrayList<>()));
            userService.saveUser(new AppUser(null, "John Smith", "jsmith", "password", new ArrayList<>()));
            userService.saveUser(new AppUser(null, "Example User", "euser", "password", new ArrayList<>()));
            userService.saveUser(new AppUser(null, "John Doe", "jdoe", "password", new ArrayList<>()));

            userService.addRoleToUser("ndzindo", "ROLE_USER");
            userService.addRoleToUser("jsmith", "ROLE_USER");
            userService.addRoleToUser("euser", "ROLE_USER");
            userService.addRoleToUser("jdoe", "ROLE_USER");
            userService.addRoleToUser("ndzindo", "ROLE_MANAGER");
            userService.addRoleToUser("jdoe", "ROLE_ADMIN");
            userService.addRoleToUser("jdoe", "ROLE_SUPER_ADMIN");

        };
    }

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Simple api for access and refresh token")
                .contact(new Contact("Nedzad Dzindo","https://github.com/ndzindo1", "dzindo_nedzad@hotmail.com"))
                .version("1.0")
                .build();
    }
}
