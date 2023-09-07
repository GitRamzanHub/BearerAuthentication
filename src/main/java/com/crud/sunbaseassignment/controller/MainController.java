package com.crud.sunbaseassignment.controller;

import com.crud.sunbaseassignment.dto.LoginDto;
import com.crud.sunbaseassignment.model.User;
import com.crud.sunbaseassignment.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class MainController{

    @Autowired
    private ApiService apiService;

    @Autowired
    private RestTemplate restTemplate;

//    ClientHttpRequestFactory requestFactory = new
//            HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        String token = apiService.authenticateUser(username, password);
        if(token.isEmpty()){

            return (ResponseEntity<String>) ResponseEntity.badRequest();
        }
        System.out.println("Received Token: "+token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addNewUser(@RequestBody User user){
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setBearerAuth("dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=");
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }
}
