package com.udd.elastic.configuration;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.udd.elastic.contract.LocationContract;

@Component
public class LocationHttpClient {
    
    @Value("${location.access_token}")
    String API_ACCESS_TOKEN;

    @Value("${location.url}")
    String API_URL;

    private RestTemplate restTemplate = new RestTemplate();

    public LocationContract getLocationFromAddress(String address) {
        var url = API_URL + API_ACCESS_TOKEN + "&q=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&format=json";
        try {
            var locationResponse = restTemplate.getForEntity(new URI(url), LocationContract[].class);
            if (locationResponse.getStatusCodeValue() != 200 || locationResponse.getBody().length == 0)
                return null;
            return locationResponse.getBody()[0];
        }
        catch (Exception e){
            return null;
        }
    }
}
