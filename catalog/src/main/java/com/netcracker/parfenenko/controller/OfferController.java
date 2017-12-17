package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.dto.OfferDto;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.mapper.OfferDtoMapper;
import com.netcracker.parfenenko.service.OfferService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/offers")
public class OfferController {

    private OfferService offerService;
    private OfferDtoMapper offerMapper;

    @Autowired
    public OfferController(OfferService offerService, OfferDtoMapper offerDtoMapper) {
        this.offerService = offerService;
        this.offerMapper = offerDtoMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new offer",
             response = OfferDto.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<OfferDto> saveOffer(@RequestBody Offer offer) {
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.save(offer)), HttpStatus.CREATED);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.findById(id)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an offer by name",
             response = OfferDto.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "There is no offer with such name")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<OfferDto> findOfferByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.findByName(name)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        try {
            return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findAll()), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.update(offer)), HttpStatus.OK) ;
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        try {
            offerService.delete(id);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    @RequestMapping(value = "/filters", method = RequestMethod.POST)
    public ResponseEntity<List<OfferDto>> findOffersByFilters(@RequestBody Map<String, List<String>> offerFilter) {
        try {
            return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findByFilter(offerFilter)),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
        try {
            return new ResponseEntity<>(offerService.findTags(id), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.changeAvailability(id)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers by tags",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOffersByTags(@RequestParam(value = "values") List<String> tags) {
        try {
            return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findOffersByTags(tags)),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for available offers",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findAvailableOffers() {
        try {
            return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findAvailableOffers()),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.addPriceToOffer(id, price)),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers of price interval",
            response = OfferDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/price", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOffersOfPriceInterval(@RequestParam(name = "from") double fromPrice,
                                                                 @RequestParam(name = "to") double toPrice) {
        try {
            return new ResponseEntity<>(offerMapper.mapEntityCollection(offerService.findOffersOfPriceInterval(fromPrice, toPrice)),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.addTagToOffer(id, tag)),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        try {
            return new ResponseEntity<>(offerMapper.mapEntity(offerService.removeTagFromOffer(id, tag)),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
