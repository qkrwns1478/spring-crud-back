package com.example.springcrudback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringCrudBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCrudBackApplication.class, args);
    }

}
