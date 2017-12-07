package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
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
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/offers")
public class OfferController {

    private OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST",
            value = "Saving a new offer",
            response = Offer.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> saveOffer(@RequestBody Offer offer) {
        try {
            return new ResponseEntity<>(offerService.save(offer), HttpStatus.CREATED);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",
            value = "Searching for an offer by id",
            response = Tag.class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "There is no offer with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> findOfferById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(offerService.findById(id), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",
            value = "Searching for an offer by name",
            response = Tag.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There is no offer with such name")
    })
    public ResponseEntity<Offer> findOfferByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(offerService.findByName(name), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",
            value = "Searching for all offers",
            response = Offer.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no existing offers")
    })
    public ResponseEntity<List<Offer>> findAllOffers() {
        try {
            return new ResponseEntity<>(offerService.findAll(), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing offer",
            response = Offer.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> updateOffer(@RequestBody Offer offer) {
        try {
            return new ResponseEntity<>(offerService.update(offer), HttpStatus.OK) ;
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(httpMethod = "DELETE",
            value = "Deleting an existing offer",
            code = 204)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> deleteOffer(@PathVariable long id) {
        try {
            offerService.delete(id);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}/tags", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",
            value = "Searching for tags of the offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Set<Tag>> findTagsOfOffer(@PathVariable long id) {
        try {
            return new ResponseEntity<>(offerService.findTags(id), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}/availability", method = RequestMethod.PUT)
    @ApiOperation(httpMethod = "PUT",
            value = "Changing availability of the offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> changeOfferAvailability(@PathVariable long id) {
        try {
            return new ResponseEntity<>(offerService.changeAvailability(id), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers by tags")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no offers with such tags")
    })
    public ResponseEntity<List<Offer>> findOffersByTags(@RequestParam(value = "values") List<String> tags) {
        try {
            return new ResponseEntity<>(offerService.findOffersByTags(tags), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/available", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",
            value = "Searching for available offers")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no available offers")
    })
    public ResponseEntity<List<Offer>> findAvailableOffers() {
        try {
            return new ResponseEntity<>(offerService.findAvailableOffers(), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/{id}/price", method = RequestMethod.PUT)
    @ApiOperation(httpMethod = "PUT",
            value = "Adding price to the existing offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> addPriceToOffer(@PathVariable long id, @RequestBody Price price) {
        try {
            return new ResponseEntity<>(offerService.addPriceToOffer(id, price), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/price", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers of price interval")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no offers of such price interval")
    })
    public ResponseEntity<List<Offer>> findOffersOfPriceInterval(@RequestParam(name = "from") double fromPrice,
                                                                 @RequestParam(name = "to") double toPrice) {
        try {
            return new ResponseEntity<>(offerService.findOffersOfPriceInterval(fromPrice, toPrice), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/{id}/tag", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST",
            value = "Adding tag to existing offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> addTagToOffer(@PathVariable long id, @RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(offerService.addTagToOffer(id, tag), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}/tag", method = RequestMethod.DELETE)
    @ApiOperation(httpMethod = "DELETE",
            value = "Removing tag from existing offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    public ResponseEntity<Offer> removeTagFromOffer(@PathVariable long id, @RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(offerService.removeTagFromOffer(id, tag), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
