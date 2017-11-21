package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/offers")
public class OfferController {

    private OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveOffer(@RequestBody Offer offer) {
        offerService.save(offer);
    }

    @RequestMapping(value = "/find_by_id", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Offer findOfferById(@RequestParam(name = "offer_id") long id) {
        return offerService.findById(id);
    }

    @RequestMapping(value = "/find_by_name", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Offer findOfferByName(@RequestParam(name = "name") String name) {
        return offerService.findByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Offer> findAllOffers() {
        return offerService.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void updateOffer(@RequestBody Offer offer) {
        offerService.update(offer);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOffer(@RequestParam(name = "offer_id") long id) {
        offerService.delete(id);
    }

    @RequestMapping(value = "/change_availability", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void changeOfferAvailability(@RequestParam(name = "offer_id") long id) {
        offerService.changeAvailability(id);
    }

    @RequestMapping(value = "/find_by_tags", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Offer> findOffersByTags(@RequestBody List<Tag> tags) {
        return offerService.findOffersByTags(tags);
    }

    @RequestMapping(value = "/find_available", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Offer> findAvailableOffers() {
        return offerService.findAvailableOffers();
    }

    @RequestMapping(value = "/add_price", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void addPriceToOffer(@RequestParam(name = "offer_id") long id, @RequestParam(name = "price") Price price) {
        offerService.addPriceToOffer(id, price);
    }

    @RequestMapping(value = "/price_interval", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Offer> findOffersOfPriceInterval(@RequestParam(name = "from_price") double fromPrice,
                                                 @RequestParam(name = "to_price") double toPrice) {
        return offerService.findOffersOfPriceInterval(fromPrice, toPrice);
    }

    @RequestMapping(value = "/add_tag", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void addTagToOffer(@RequestParam(name = "offer_id") long id, @RequestParam(name = "tag") Tag tag) {
        offerService.addTagToOffer(id, tag);
    }

    @RequestMapping(value = "/remove_tag", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void removeTagFromOffer(@RequestParam(name = "offer_id") long id, @RequestParam(name = "tag") Tag tag) {
        offerService.removeTagFromOffer(id, tag);
    }

    @RequestMapping(value = "/add_to_category", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void addOfferToCategory(@RequestParam(name = "offer_id") long offerId,
                                   @RequestParam(name = "category_id") long categoryId) {
        offerService.addOfferToCategory(offerId, categoryId);
    }

    @RequestMapping(value = "/remove_from_category", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void removeOfferFromCategory(@RequestParam(name = "offer_id") long id) {
        offerService.removeOfferFromCategory(id);
    }
    
}
