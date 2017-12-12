package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.client.OfferClient;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.filter.OfferFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OfferService {

    private OfferClient offerClient;

    @Autowired
    public OfferService(OfferClient offerClient) {
        this.offerClient = offerClient;
    }

    public ResponseEntity<Offer[]> findOffers(OfferFilter offerFilter) {
        return offerClient.findOffers(getOfferFiltersString(offerFilter));
    }

    public ResponseEntity<Offer> findOfferById(long offerId) {
        return offerClient.findOfferById(offerId);
    }

    private String getOfferFiltersString(OfferFilter offerFilter) {
        StringBuilder filters = new StringBuilder("?");
        if (offerFilter.getCategories() != null) {
            String categories = offerFilter.getCategories().toString();
            filters
                    .append("categories=")
                    .append(categories.substring(1, categories.length() - 1))
                    .append("&");
        }
        if (offerFilter.getTags() != null) {
            String tags = offerFilter.getTags().toString();
            filters
                    .append("tags=")
                    .append(tags.substring(1, tags.length() - 1))
                    .append("&");
        }
        filters.append("from=").append(offerFilter.getFrom())
                .append("&to=").append(offerFilter.getTo());
        return filters.toString();
    }

}
