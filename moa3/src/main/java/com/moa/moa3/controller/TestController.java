package com.moa.moa3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @GetMapping
    public ResponseEntity basicRequestTest() {
        Random rand = new Random();
        int randomNum = rand.nextInt(100);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(randomNum);
    }


}
