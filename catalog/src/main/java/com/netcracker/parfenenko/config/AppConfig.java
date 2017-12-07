package com.netcracker.parfenenko.config;

import com.netcracker.parfenenko.entities.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    @Scope(value = "prototype")
    public Tag tag() {
        return new Tag();
    }

}
