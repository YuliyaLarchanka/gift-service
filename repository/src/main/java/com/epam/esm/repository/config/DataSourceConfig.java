package com.epam.esm.repository.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
//    @Bean
//    public DataSource datasource() {
//        return DataSourceBuilder.create()
//                .driverClassName("org.postgresql.Driverr")
//                .url("jdbc:postgresql://localhost:5432/gift_service")
//                .username("postgres")
//                .password("StrongPassword!")
//                .build();
//    }
}
