package tn.esprit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @GetMapping("/api/health")
    public String health() {
        return "JavaFX App is running with Spring Boot API";
    }

    @GetMapping("/api/users")
    public String getUsers() {
        return "List of users from JavaFX app";
    }
}