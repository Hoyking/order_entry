package com.netcracker.parfenenko.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/v1")
public class WebController {

    @GetMapping("/")
    public String catalog() {
        return "catalog";
    }

}
