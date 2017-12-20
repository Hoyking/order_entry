package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.client.OfferClient;
import com.netcracker.parfenenko.entity.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OfferService {

    private OfferClient offerClient;

    @Autowired
    public OfferService(OfferClient offerClient) {
        this.offerClient = offerClient;
    }

    public ResponseEntity<Offer[]> findOffers(Map<String, List<String>> offerFilter) {
        return offerClient.findOffers(offerFilter);
    }

    public ResponseEntity<Offer[]> findOffersByPartOfName(String part) {
        return offerClient.findOffersByPartOfName(part);
    }

    public ResponseEntity<Offer> findOfferById(long offerId) {
        return offerClient.findOfferById(offerId);
    }

}
