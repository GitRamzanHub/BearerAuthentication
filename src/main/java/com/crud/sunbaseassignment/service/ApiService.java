package com.crud.sunbaseassignment.service;

import com.crud.sunbaseassignment.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.authentication-url}")
    private String authenticationUrl;

    @Value("${api.create-customer-url}")
    private String createCustomerUrl;

    private String bearerToken;

    public String authenticateUser(String username, String password) {

        // create the request body as map
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login_id", username);
        requestBody.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                authenticationUrl ,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // check if the response status is OK (HTTP 200)
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            // Parse the token from the response body
            String responseBody = responseEntity.getBody();

            // Assuming the token is in a JSON response with a field named token
            String token = parseTokenFromResponse(responseBody);
            System.out.println(token);

            if(token != null){
                bearerToken = "Bearer "+token;
                return bearerToken;
            }
        }
        return "Invalid UserDetails";
    }

    // You may need to implement a method to parse the token from the response body
    private String parseTokenFromResponse(String responseBody) {
        // Implement logic to parse the token from the response body
        // Return the token or null if not found
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Assuming the token is in a field named "token" in the JSON response
            JsonNode tokenNode = jsonNode.get("access_token");

            if (tokenNode != null && tokenNode.isTextual()) {
                return tokenNode.asText();
            }
        } catch (Exception e) {
            // Handle any parsing exceptions here
            e.printStackTrace();
        }
        return null;
    }

    public boolean createCustomer(User user) { // can use User object here

        return true; // Modify based on your implementation
    }

    public List<User> getCustomerList() {
        // Implement logic to get the customer list using restTemplate
        // Parse the response and return a list of customers
        return Collections.singletonList(new User()); // Modify based on your implementation
    }
}
