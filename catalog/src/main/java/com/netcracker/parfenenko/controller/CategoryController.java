package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.dto.CategoryDto;
import com.netcracker.parfenenko.dto.OfferDto;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.mapper.CategoryDtoMapper;
import com.netcracker.parfenenko.mapper.OfferDtoMapper;
import com.netcracker.parfenenko.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {

    private CategoryService categoryService;
    private CategoryDtoMapper categoryMapper;
    private OfferDtoMapper offerMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryDtoMapper categoryMapper, OfferDtoMapper offerMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.offerMapper = offerMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new category",
            response = CategoryDto.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryMapper.mapEntity(categoryService.save(category)), HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for a category by id",
            response = CategoryDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "There is now category with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable long id) {
        return new ResponseEntity<>(categoryMapper.mapEntity(categoryService.findById(id)), HttpStatus.OK);

    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for a category by name",
            response = CategoryDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "There is now category with such name"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<CategoryDto> findCategoryByName(@PathVariable String name) {
        return new ResponseEntity<>(categoryMapper.mapEntity(categoryService.findByName(name)), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all categories",
            response = CategoryDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> findAllCategories() {
        return new ResponseEntity<>(categoryMapper.mapEntityCollection(categoryService.findAll()), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing category",
            response = CategoryDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Category doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryMapper.mapEntity(categoryService.update(category)), HttpStatus.OK) ;
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Deleting an existing category",
            code = 204)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Category doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers of a category",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Category not found"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/offers", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findCategoryOffers(@PathVariable long id) {
        return new ResponseEntity<>(offerMapper.mapEntityCollection(categoryService.findCategoryOffers(id)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Adding an offer to the category",
            response = CategoryDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Either Category or offer not found"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/offer", method = RequestMethod.PUT)
    public ResponseEntity<CategoryDto> addOfferToCategory(@PathVariable(name = "id") long categoryId,
                                                          @RequestBody long offerId) {
        return new ResponseEntity<>(categoryMapper.mapEntity(categoryService.addOffer(categoryId, offerId)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Deleting an offer from the category",
            response = CategoryDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Either category or offer not found"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/offer", method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDto> removeOfferFromCategory(@PathVariable(name = "id") long categoryId,
                                                            @RequestBody long offerId) {
        return new ResponseEntity<>(categoryMapper.mapEntity(categoryService.removeOffer(categoryId, offerId)),
                HttpStatus.OK);
    }

}
