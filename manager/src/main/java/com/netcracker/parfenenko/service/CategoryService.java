package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.client.CategoryClient;
import com.netcracker.parfenenko.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private CategoryClient categoryClient;

    @Autowired
    public CategoryService(CategoryClient categoryClient) {
        this.categoryClient = categoryClient;
    }

    public ResponseEntity<Category[]> findAll() {
        return categoryClient.findAll();
    }

}
