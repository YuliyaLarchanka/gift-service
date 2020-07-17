package com.epam.esm.web;

//import com.epam.esm.web.config.SpringAppConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
}