package postman.bottler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class BottlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BottlerApplication.class, args);
    }

}
