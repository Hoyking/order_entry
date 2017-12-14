package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.dto.OfferDto;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.service.TagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {

    private TagService tagService;
    private ModelMapper modelMapper;

    @Autowired
    public TagController(TagService tagService, ModelMapper modelMapper) {
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new tag",
            response = Tag.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Tag> saveTag(@RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(tagService.save(tag), HttpStatus.CREATED);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for a tag by id",
            response = Tag.class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "There is no tag with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Tag> findTagById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for a tag by name",
            response = Tag.class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "There is no tag with such name"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Tag> findTagByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(tagService.findByName(name), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all tags",
            response = Tag.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no existing tags")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Tag>> findAllTags() {
        try {
            return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing tag",
            response = Tag.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Tag doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(tagService.update(tag), HttpStatus.OK) ;
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
    public ResponseEntity<Tag> deleteTag(@PathVariable long id) {
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
            response = Offer.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Tag not found"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/name/{name}/offers", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findTagOffers(@PathVariable String name) {
        try {
            return new ResponseEntity<>(convertFromOfferList(tagService.findTagOffers(name)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private List<OfferDto> convertFromOfferList(List<Offer> offers) {
        List<OfferDto> offerDtos = new ArrayList<>();
        offers.forEach(offer -> offerDtos.add(modelMapper.map(offer, OfferDto.class)));
        return offerDtos;
    }

}