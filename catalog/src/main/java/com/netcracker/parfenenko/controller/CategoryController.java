package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.save(category), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Category> findCategoryById(@PathVariable long id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Category> findCategoryByName(@PathVariable String name) {
        return new ResponseEntity<>(categoryService.findByName(name), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Category>> findAllCategories() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.update(category), HttpStatus.OK) ;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(@PathVariable long id) {
        categoryService.delete(id);
        Category category = null;
        return new ResponseEntity<>(category, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}/offers", method = RequestMethod.GET)
    public ResponseEntity<List<Offer>> findCategoryOffers(@PathVariable long id) {
        return new ResponseEntity<>(categoryService.findCategoryOffers(id), HttpStatus.OK);
    }

}
