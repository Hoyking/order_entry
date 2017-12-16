package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.dto.OfferDto;
import com.netcracker.parfenenko.dto.TagDto;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.mapper.OfferDtoMapper;
import com.netcracker.parfenenko.mapper.TagDtoMapper;
import com.netcracker.parfenenko.service.TagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {

    private TagService tagService;
    private TagDtoMapper tagMapper;
    private OfferDtoMapper offerMapper;

    @Autowired
    public TagController(TagService tagService, TagDtoMapper tagMapper, OfferDtoMapper offerMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
        this.offerMapper = offerMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new tag",
            response = TagDto.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TagDto> saveTag(@RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(tagMapper.mapEntity(tagService.save(tag)), HttpStatus.CREATED);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for a tag by id",
            response = TagDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "There is no tag with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TagDto> findTagById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(tagMapper.mapEntity(tagService.findById(id)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for a tag by name",
            response = TagDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "There is no tag with such name"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<TagDto> findTagByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(tagMapper.mapEntity(tagService.findByName(name)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all tags",
            response = TagDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TagDto>> findAllTags() {
        try {
            return new ResponseEntity<>(tagMapper.mapEntityCollection(tagService.findAll()), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing tag",
            response = TagDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Tag doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<TagDto> updateTag(@RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(tagMapper.mapEntity(tagService.update(tag)), HttpStatus.OK) ;
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Deleting an existing tag")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Tag doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<TagDto> deleteTag(@PathVariable long id) {
        try {
            tagService.delete(id);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers of a tag",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Tag not found"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/name/{name}/offers", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findTagOffers(@PathVariable String name) {
        try {
            return new ResponseEntity<>(offerMapper.mapEntityCollection(tagService.findTagOffers(name)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}