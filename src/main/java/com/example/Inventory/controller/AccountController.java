package com.example.Inventory.controller;

import com.example.Inventory.constants.Path;
import com.example.Inventory.dto.account.RegisterFormDTO;
import com.example.Inventory.dto.account.UserRepDTO;
import com.example.Inventory.exception.CustomRuntimeException;
import com.example.Inventory.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
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

    // get log in user
    @GetMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }

    // get all users
    @GetMapping(Path.admin + "/users")
    public ResponseEntity<List<UserRepDTO>> listUsers() {
        List<UserRepDTO> res = service.userList();
        return ResponseEntity.ok(res);
    }

    // get all supplier
    @GetMapping(Path.admin + "/supplier")
    public ResponseEntity<List<UserRepDTO>> listSupplier() {
        List<UserRepDTO> res = service.supplierList();
        return ResponseEntity.ok(res);
    }

    // register user
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterFormDTO payload) {
        service.register(payload);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/test/{id}")
    public void test(@PathVariable String id) {

    }



}
