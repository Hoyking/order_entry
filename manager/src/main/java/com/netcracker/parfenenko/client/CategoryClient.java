package com.netcracker.parfenenko.client;

import com.netcracker.parfenenko.entity.Category;
import com.netcracker.parfenenko.util.RequestManager;
import com.netcracker.parfenenko.util.UriProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryClient {

    private RequestManager requestManager;
    private UriProvider uriProvider;

    private final String URN = "catalog";

    @Autowired
    public CategoryClient(RequestManager requestManager, UriProvider uriProvider) {
        this.requestManager = requestManager;
        this.uriProvider = uriProvider;
    }

    public ResponseEntity<Category[]> findAll() {
        return requestManager.getRequest(uriProvider.get(URN, "categories"), Category[].class);
    }

}
