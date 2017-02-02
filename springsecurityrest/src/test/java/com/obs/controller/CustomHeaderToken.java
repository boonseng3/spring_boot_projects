package com.obs.controller;

import com.obs.ApplicationServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ongbo on 2/2/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.config.name=server-test"}, classes = {ApplicationServer.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomHeaderToken {
    @Autowired // pre-configured to resolve relative paths to http://localhost:${local.server.port}
    private TestRestTemplate restTemplate;

    @Test
    public void unauthorizedStatusCodeForUnauthenticatedRequest() {

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/ping", String.class);
        assertThat(response).hasFieldOrPropertyWithValue("statusCode", HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void successfulLogin() {

        MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.add("username", "user");
        request.add("password", "P@ssw0rd");
        ResponseEntity<Void> response = restTemplate.postForEntity("/login", request, Void.class);

        assertThat(response).hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK);

        System.out.println("response = " + response.getHeaders().get("x-auth-token").get(0));
        // verify header
        assertThat(response.getHeaders().get("x-auth-token")).hasSize(1)
                .element(0).asString().matches(Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"));
    }

    @Test
    public void unsuccessfulLogin() {

        MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.add("username", "user");
        request.add("password", "P@ssw0rd1");
        ResponseEntity<Void> response = restTemplate.postForEntity("/login", request, Void.class);

        assertThat(response).hasFieldOrPropertyWithValue("statusCode", HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void successfulPing() {

        MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.add("username", "user");
        request.add("password", "P@ssw0rd");
        ResponseEntity<Void> response = restTemplate.postForEntity("/login", request, Void.class);

        assertThat(response).hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK);

        String token = response.getHeaders().get("x-auth-token").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-token", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> pingResponse = restTemplate.exchange("/api/v1/ping", HttpMethod.GET, entity, String.class);
        assertThat(pingResponse).hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK)
                .hasFieldOrPropertyWithValue("body", "Ping response");
    }
}
