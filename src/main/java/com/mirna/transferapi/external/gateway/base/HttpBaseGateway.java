package com.mirna.transferapi.external.gateway.base;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpBaseGateway {

    @Autowired
    protected RestTemplate restTemplate;

    public ResponseEntity<Map> get(String URL)  {
        return restTemplate.getForEntity(URL, Map.class);
    }

    public ResponseEntity<Map> post(String URL, Object data) {
        return restTemplate.postForEntity(URL, data, Map.class);
    }

    public ResponseEntity<Map> put(String URL, Object data) {
        return  null;
    }

    public ResponseEntity<Map> patch(String URL, Object data) {
        return null;
    }

    public ResponseEntity<Map> delete(String URL) {
        return null;
    }
}
