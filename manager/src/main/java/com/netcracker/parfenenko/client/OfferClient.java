package com.netcracker.parfenenko.client;

import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.util.RequestManager;
import com.netcracker.parfenenko.util.UriProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OfferClient {

    private RequestManager requestManager;
    private UriProvider uriProvider;

    @Autowired
    public OfferClient(RequestManager requestManager, UriProvider uriProvider) {
        this.requestManager = requestManager;
        this.uriProvider = uriProvider;
    }

    public ResponseEntity<Offer[]> findOffers(String filters) {
        return requestManager.getRequest(String.format(uriProvider.get("findOffersByFiltersURI"), filters),
                Offer[].class);
    }

    public ResponseEntity<Offer> findOfferById(long offerId) {
        return requestManager.getRequest(String.format(uriProvider.get("baseOfferURI"), offerId),
                Offer.class);
    }

}
