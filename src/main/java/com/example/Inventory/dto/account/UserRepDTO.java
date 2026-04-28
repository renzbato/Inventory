package com.example.Inventory.dto.account;

import lombok.Builder;

import java.util.List;

@Builder
public record UserRepDTO(
        String id,
        String email,
        String firstName,
        String lastName,
        List<String> roles
) {
}
