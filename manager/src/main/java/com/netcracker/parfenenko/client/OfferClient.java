package com.netcracker.parfenenko.client;

import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.util.RequestManager;
import com.netcracker.parfenenko.util.UriProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OfferClient {

    private RequestManager requestManager;
    private UriProvider uriProvider;

    private final String URN = "catalog";

    @Autowired
    public OfferClient(RequestManager requestManager, UriProvider uriProvider) {
        this.requestManager = requestManager;
        this.uriProvider = uriProvider;
    }

    public ResponseEntity<Offer[]> findOffers(Map<String, List<String>> offerFilter) {
        return requestManager.postRequest(uriProvider.get(URN, "offers/filters"), new HttpEntity<>(offerFilter),
                Offer[].class);
    }

    public ResponseEntity<Offer> findOfferById(long offerId) {
        return requestManager.getRequest(String.format(uriProvider.get(URN, "offers/%s"), offerId),
                Offer.class);
    }

}
