package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveCategory(@RequestBody Category category) {
        categoryService.save(category);
    }

    @RequestMapping(value = "/find_by_id", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Category findCategoryById(@RequestParam(name = "category_id") long id) {
        return categoryService.findById(id);
    }

    @RequestMapping(value = "/find_by_name", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Category findCategoryByName(@RequestParam(name = "name") String name) {
        return categoryService.findByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Category> findAllCategories() {
        return categoryService.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void updateCategory(@RequestBody Category category) {
        categoryService.update(category);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCategory(@RequestParam(name = "category_id") long id) {
        categoryService.delete(id);
    }

    @RequestMapping(value = "/find_offers", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Offer> findCategoryOffers(@RequestParam(name = "category_id") long id) {
        return categoryService.findCategoryOffers(id);
    }

}
