package com.netcracker.parfenenko.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestManager {

    private RestTemplate restTemplate;
    
    @Autowired
    public RequestManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public <T> ResponseEntity<T> getRequest(String uri, Class<T> responseType) {
        return restTemplate.getForEntity(uri, responseType);
    }

    public <T, R> ResponseEntity<T> postRequest(String uri, HttpEntity<R> httpEntity, Class<T> responseType) {
        return restTemplate.postForEntity(uri, httpEntity, responseType);
    }

    public <T, R> ResponseEntity<T> deleteRequest(String uri, HttpEntity<R> httpEntity, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, responseType);
    }

    public <T, R> ResponseEntity<T> putRequest(String uri, HttpEntity<R> httpEntity, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, responseType);
    }

    public <T> ResponseEntity<T> putRequest(String uri, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.PUT, null, responseType);
    }
    
}
