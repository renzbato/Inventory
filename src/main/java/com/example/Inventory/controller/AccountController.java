package com.example.Inventory.controller;

import com.example.Inventory.constants.Path;
import com.example.Inventory.dto.account.RegisterFormDTO;
import com.example.Inventory.dto.account.UserInfoDTO;
import com.example.Inventory.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @GetMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserInfoDTO>> getUser() {
        List<UserInfoDTO> res = service.listAllUsers();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterFormDTO payload) {
        service.register(payload);
        return ResponseEntity.ok("User registered successfully");
    }


}
