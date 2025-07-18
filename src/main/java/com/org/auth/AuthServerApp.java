package com.org.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServerApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AuthServerApp.class);
        app.run(args);
    }
}
