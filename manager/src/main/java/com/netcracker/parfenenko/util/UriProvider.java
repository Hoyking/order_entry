package com.netcracker.parfenenko.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class UriProvider {

    private Map<String, String> uriMap;

    public UriProvider() {
        loadURIs();
    }

    private void loadURIs() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            uriMap = mapper.readValue(new File("manager/src/main/resources/uri.yml"), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String uriName) {
        return uriMap.get(uriName);
    }

}
