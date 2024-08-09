package com.example.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DisplayController {
    @GetMapping("/user/chat")
    public String index(){
        return "client.html";
    }
}
