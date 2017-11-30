package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/offers")
public class OfferController {

    private OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Offer> saveOffer(@RequestBody Offer offer) {
        return new ResponseEntity<>(offerService.save(offer), HttpStatus.CREATED);
    }

//    @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType="path", dataTypeClass = Long.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Offer> findOfferById(@PathVariable Long id) {
        return new ResponseEntity<>(offerService.findById(id), HttpStatus.OK);
    }

//    @ApiImplicitParam(name = "name", value = "?????", required = true, dataType = "String", paramType="path")
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Offer> findOfferByName(@PathVariable String name) {
        return new ResponseEntity<>(offerService.findByName(name), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Offer>> findAllOffers() {
        return new ResponseEntity<>(offerService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Offer> updateOffer(@RequestBody Offer offer) {
        return new ResponseEntity<>(offerService.update(offer), HttpStatus.OK) ;
    }

//    @ApiImplicitParam(name = "id", value = "?????", required = true, dataType = "Long", paramType="path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Offer> deleteOffer(@PathVariable long id) {
        offerService.delete(id);
        Offer offer = null;
        return new ResponseEntity<>(offer, HttpStatus.NO_CONTENT);
    }

//    @ApiImplicitParam(name = "id", value = "?????", required = true, dataType = "Long", paramType="path")
    @RequestMapping(value = "/{id}/availability", method = RequestMethod.PUT)
    public ResponseEntity<Offer> changeOfferAvailability(@PathVariable long id) {
        return new ResponseEntity<>(offerService.changeAvailability(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public ResponseEntity<List<Offer>> findOffersByTags(@RequestBody List<Tag> tags) {
        return new ResponseEntity<>(offerService.findOffersByTags(tags), HttpStatus.OK);
    }

    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public ResponseEntity<List<Offer>> findAvailableOffers() {
        return new ResponseEntity<>(offerService.findAvailableOffers(), HttpStatus.OK);
    }

//    @ApiImplicitParam(name = "id", value = "?????", required = true, dataType = "Long", paramType="path")
    @RequestMapping(value = "/{id}/price", method = RequestMethod.PUT)
    public ResponseEntity<Offer> addPriceToOffer(@PathVariable long id, @RequestBody Price price) {
        return new ResponseEntity<>(offerService.addPriceToOffer(id, price), HttpStatus.OK);
    }

    @RequestMapping(value = "/price", method = RequestMethod.GET)
    public ResponseEntity<List<Offer>> findOffersOfPriceInterval(@RequestParam(name = "from") double fromPrice,
                                                                 @RequestParam(name = "to") double toPrice) {
        return new ResponseEntity<>(offerService.findOffersOfPriceInterval(fromPrice, toPrice), HttpStatus.OK);
    }

//    @ApiImplicitParam(name = "id", value = "?????", required = true, dataType = "Long", paramType="path")
    @RequestMapping(value = "/{id}/tag", method = RequestMethod.POST)
    public ResponseEntity<Offer> addTagToOffer(@PathVariable long id, @RequestBody Tag tag) {
        return new ResponseEntity<>(offerService.addTagToOffer(id, tag), HttpStatus.OK);
    }

//    @ApiImplicitParam(name = "id", value = "?????", required = true, dataType = "Long", paramType="path")
    @RequestMapping(value = "/{id}/tag", method = RequestMethod.DELETE)
    public ResponseEntity<Offer> removeTagFromOffer(@PathVariable long id, @RequestBody Tag tag) {
        return new ResponseEntity<>(offerService.removeTagFromOffer(id, tag), HttpStatus.OK);
    }

}
