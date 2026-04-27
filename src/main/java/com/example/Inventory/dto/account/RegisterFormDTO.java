package com.example.Inventory.dto.account;

public record RegisterFormDTO(
        String username,
        String firstname,
        String lastname,
        String email,
        String password,
        String confirmPassword
) {
}
