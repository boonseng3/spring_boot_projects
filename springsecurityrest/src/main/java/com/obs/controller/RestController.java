package com.obs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ongbo on 2/2/2017.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
public class RestController {

    @GetMapping(value = "/ping")
    public ResponseEntity<String> echo() {
        return new ResponseEntity<String>("Ping response", HttpStatus.OK);
    }
}
