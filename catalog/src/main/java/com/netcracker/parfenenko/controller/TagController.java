package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveTag(@RequestBody Tag tag) {
        tagService.save(tag);
    }

    @RequestMapping(value = "/find_by_id", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Tag findTagById(@RequestParam(name = "tag_id") long id) {
        return tagService.findById(id);
    }

    @RequestMapping(value = "/find_by_name", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Tag findTagByName(@RequestParam(name = "name") String name) {
        return tagService.findByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Tag> findAllTags() {
        return tagService.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void updateTag(@RequestBody Tag tag) {
        tagService.update(tag);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTag(@RequestParam(name = "tag_id") long id) {
        tagService.delete(id);
    }

    @RequestMapping(value = "/find_offers", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Offer> findTagOffers(@RequestParam(name = "tag_id") long id) {
        return tagService.findTagOffers(id);
    }

}