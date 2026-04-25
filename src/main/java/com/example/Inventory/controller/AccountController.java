package com.example.Inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/account")
@RequiredArgsConstructor
public class AccountController {

    @GetMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }
}
