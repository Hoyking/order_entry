package com.netcracker.parfenenko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.netcracker.parfenenko.dao", "com.netcracker.parfenenko.service",
                                           "com.netcracker.parfenenko.controller", "com.netcracker.parfenenko.util"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
