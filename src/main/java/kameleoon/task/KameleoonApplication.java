package kameleoon.task;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Kameleoon test task",
                version = "1.0",
                description = "Site for posting quotes"
        )
)
public class KameleoonApplication {
    public static void main(String[] args) {
        SpringApplication.run(KameleoonApplication.class, args);
    }
}
