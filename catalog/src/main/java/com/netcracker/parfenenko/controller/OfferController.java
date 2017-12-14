package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.dto.OfferDto;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.filter.OfferFilter;
import com.netcracker.parfenenko.service.OfferService;
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
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/offers")
public class OfferController {

    private OfferService offerService;
    private ModelMapper modelMapper;

    @Autowired
    public OfferController(OfferService offerService, ModelMapper modelMapper) {
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new offer",
            response = Offer.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<OfferDto> saveOffer(@RequestBody Offer offer) {
        try {
            return new ResponseEntity<>(modelMapper.map(offerService.save(offer), OfferDto.class), HttpStatus.CREATED);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an offer by id",
            response = Offer.class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "There is no offer with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OfferDto> findOfferById(@PathVariable long id) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(offerService.findById(id), OfferDto.class), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an offer by name",
            response = Offer.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There is no offer with such name")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<OfferDto> findOfferByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(modelMapper.map(offerService.findByName(name), OfferDto.class), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all offers",
            response = Offer.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no existing offers")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findAllOffers() {
        try {
            return new ResponseEntity<>(convertFromOfferList(offerService.findAll()), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing offer",
            response = Offer.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<OfferDto> updateOffer(@RequestBody Offer offer) {
        try {
            return new ResponseEntity<>(modelMapper.map(offerService.update(offer), OfferDto.class), HttpStatus.OK) ;
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

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers with some filters",
            response = Offer.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no offers with such filters")
    })
    @RequestMapping(value = "/filters", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOffersByFilters(@ModelAttribute(name = "filters") OfferFilter offerFilter) {
        try {
            return new ResponseEntity<>(convertFromOfferList(offerService.findByFilter(offerFilter)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for tags of the offer")
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
            value = "Changing availability of the offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/availability", method = RequestMethod.PUT)
    public ResponseEntity<OfferDto> changeOfferAvailability(@PathVariable long id) {
        try {
            return new ResponseEntity<>(modelMapper.map(offerService.changeAvailability(id), OfferDto.class),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers by tags")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no offers with such tags")
    })
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOffersByTags(@RequestParam(value = "values") List<String> tags) {
        try {
            return new ResponseEntity<>(convertFromOfferList(offerService.findOffersByTags(tags)), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for available offers")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no available offers")
    })
    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findAvailableOffers() {
        try {
            return new ResponseEntity<>(convertFromOfferList(offerService.findAvailableOffers()), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Adding price to the existing offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/price", method = RequestMethod.PUT)
    public ResponseEntity<OfferDto> addPriceToOffer(@PathVariable long id, @RequestBody Price price) {
        try {
            return new ResponseEntity<>(modelMapper.map(offerService.addPriceToOffer(id, price), OfferDto.class),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers of price interval")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no offers of such price interval")
    })
    @RequestMapping(value = "/price", method = RequestMethod.GET)
    public ResponseEntity<List<OfferDto>> findOffersOfPriceInterval(@RequestParam(name = "from") double fromPrice,
                                                                 @RequestParam(name = "to") double toPrice) {
        try {
            return new ResponseEntity<>(convertFromOfferList(offerService.findOffersOfPriceInterval(fromPrice, toPrice)),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Adding tag to existing offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/tag", method = RequestMethod.POST)
    public ResponseEntity<OfferDto> addTagToOffer(@PathVariable long id, @RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(modelMapper.map(offerService.addTagToOffer(id, tag), OfferDto.class),
                    HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Removing tag from existing offer")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/tag", method = RequestMethod.DELETE)
    public ResponseEntity<OfferDto> removeTagFromOffer(@PathVariable long id, @RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(modelMapper.map(offerService.removeTagFromOffer(id, tag), OfferDto.class),
                    HttpStatus.OK);
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
