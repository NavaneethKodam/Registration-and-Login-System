package com.nava.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/Navaneeth")
    public String hello()
    {
        return "Hello Navaneeth, Welcome to Google !";
    }
}
