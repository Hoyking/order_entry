package com.netcracker.parfenenko.config;

import com.netcracker.parfenenko.entity.OrderItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @Scope(value = "prototype")
    public OrderItem orderItem() {
        return new OrderItem();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
