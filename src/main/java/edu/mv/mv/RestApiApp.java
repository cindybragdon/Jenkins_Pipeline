package edu.mv.mv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableJpaRepositories("edu.mv.mv.repository")

public class RestApiApp {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApp.class, args);
    }
}
