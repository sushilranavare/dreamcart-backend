/*
* Main entry point of the DreamCart backend application.
* Springboot starts the application frpm here and loads all configurations, compoonents and entities and repositories.
* */
package com.dreamcart.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class    DreamcartBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamcartBackendApplication.class, args);
    }

}
