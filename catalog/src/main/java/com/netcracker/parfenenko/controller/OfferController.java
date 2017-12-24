package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.dto.FreshOfferDto;
import com.netcracker.parfenenko.dto.OfferDto;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.mapper.FreshOfferDtoMapper;
import com.netcracker.parfenenko.mapper.OfferDtoMapper;
import com.netcracker.parfenenko.service.OfferService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/offers")
public class OfferController {

    private OfferService offerService;
    private OfferDtoMapper offerMapper;
    private FreshOfferDtoMapper freshOfferMapper;

    @Autowired
    public OfferController(OfferService offerService, OfferDtoMapper offerDtoMapper, FreshOfferDtoMapper freshOfferDtoMapper) {
        this.offerService = offerService;
        this.offerMapper = offerDtoMapper;
        this.freshOfferMapper = freshOfferDtoMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new offer",
             response = OfferDto.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<OfferDto> saveOffer(@RequestBody FreshOfferDto offer) {
        return new ResponseEntity<>(offerMapper
                .mapEntity(offerService
                        .save(freshOfferMapper.mapDto(offer))),
                HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an offer by id",
             response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "There is no offer with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OfferDto> findOfferById(@PathVariable long id) {
        return new ResponseEntity<>(offerMapper.mapEntity(offerService.findById(id)), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an offer by name",
             response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "There is no offer with such name")
    })
    @RequestMapping(params = {"name"}, method = RequestMethod.GET)
    public ResponseEntity<OfferDto> findOfferByName(@RequestParam(name = "name") String name) {
        return new ResponseEntity<>(offerMapper.mapEntity(offerService.findByName(name)), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers by part of name",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(params = {"namePart"}, method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOfferByPartOfName(@ApiParam(name = "namePart") String namePart) {
        return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findByPartOfName(namePart)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all offers",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findAllOffers() {
        return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findAll()), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing offer",
             response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<OfferDto> updateOffer(@RequestBody Offer offer) {
        return new ResponseEntity<>(offerMapper.mapEntity(offerService.update(offer)), HttpStatus.OK) ;
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Deleting an existing offer",
            code = 204)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<OfferDto> deleteOffer(@PathVariable long id) {
        offerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Searching for offers with some filters",
             response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 400, message = "Wrong filters")
    })
    @RequestMapping(value = "/filteredOffers", method = RequestMethod.POST)
    public ResponseEntity<List<OfferDto>> findOffersByFilters(@RequestBody Map<String, List<String>> offerFilter) {
        return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findByFilter(offerFilter)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for tags of the offer",
            response = Tag.class,
            responseContainer = "Set")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/tags", method = RequestMethod.GET)
    public ResponseEntity<Set<Tag>> findTagsOfOffer(@PathVariable long id) {
        return new ResponseEntity<>(offerService.findTags(id), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Changing availability of the offer",
            response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/availability", method = RequestMethod.PUT)
    public ResponseEntity<OfferDto> changeOfferAvailability(@PathVariable long id) {
        return new ResponseEntity<>(offerMapper.mapEntity(offerService.changeAvailability(id)), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers by tags",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(params = {"tags"}, method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOffersByTags(@RequestParam(name = "tags") List<String> tags) {
        return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findByTags(tags)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for available offers",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/availableOffers", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findAvailableOffers() {
        return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findAvailableOffers()),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Adding price to the existing offer",
            response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/price", method = RequestMethod.PUT)
    public ResponseEntity<OfferDto> addPriceToOffer(@PathVariable long id, @RequestBody Price price) {
        return new ResponseEntity<>(offerMapper.mapEntity(offerService.updatePrice(id, price)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers of price interval",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(params = {"fromPrice", "toPrice"}, method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOffersOfPriceInterval(@RequestParam(name = "fromPrice") double fromPrice,
                                                                    @RequestParam(name = "toPrice") double toPrice) {
        return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findOffersOfPriceInterval(fromPrice, toPrice)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Adding tag to existing offer",
            response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/tag", method = RequestMethod.POST)
    public ResponseEntity<OfferDto> addTagToOffer(@PathVariable long id, @RequestBody Tag tag) {
        return new ResponseEntity<>(offerMapper.mapEntity(offerService.addTagToOffer(id, tag)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Removing tag from existing offer",
            response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/tag", method = RequestMethod.DELETE)
    public ResponseEntity<OfferDto> removeTagFromOffer(@PathVariable long id, @RequestBody Tag tag) {
        return new ResponseEntity<>(offerMapper.mapEntity(offerService.removeTagFromOffer(id, tag)),
                HttpStatus.OK);
    }

}
