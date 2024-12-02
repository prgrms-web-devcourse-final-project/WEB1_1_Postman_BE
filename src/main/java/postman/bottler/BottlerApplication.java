package postman.bottler;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BottlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BottlerApplication.class, args);
    }

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.load();

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }

}
