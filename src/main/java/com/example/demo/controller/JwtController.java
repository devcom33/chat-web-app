package com.example.demo.controller;

import com.example.demo.sec.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class JwtController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/decode")
    public String decodeToken(@RequestParam String token) {
        try {
            // Decode token
            String username = jwtUtil.getUsernameFromToken(token);
            Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
            boolean isValid = jwtUtil.validateToken(token);

            // Return the results
            return String.format("Username: %s, Expiration Date: %s, Is Valid: %b",
                    username, expirationDate, isValid);
        } catch (Exception e) {
            return "Invalid token or error occurred.";
        }
    }
}