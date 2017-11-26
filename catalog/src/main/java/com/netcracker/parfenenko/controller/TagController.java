package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Tag> saveTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(tagService.save(tag), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Tag> findTagById(@PathVariable long id) {
        return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Tag> findTagByName(@PathVariable String name) {
        return new ResponseEntity<>(tagService.findByName(name), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Tag>> findAllTags() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(tagService.update(tag), HttpStatus.OK) ;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Tag> deleteTag(@PathVariable long id) {
        tagService.delete(id);
        Tag tag = null;
        return new ResponseEntity<>(tag, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}/offers", method = RequestMethod.GET)
    public ResponseEntity<List<Offer>> findTagOffers(@PathVariable long id) {
        return new ResponseEntity<>(tagService.findTagOffers(id), HttpStatus.OK);
    }

}