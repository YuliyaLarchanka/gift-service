package com.epam.esm.web;

//import com.epam.esm.web.config.SpringAppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
//@ComponentScan(basePackages = "com.epam.esm")
//@Import(SpringAppConfig.class)
public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
}