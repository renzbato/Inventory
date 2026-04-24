package com.example.Inventory.dto;

import jakarta.validation.constraints.Min;

public record PurchaseRequestDTO(
        int productId,
        int quantity
) {
}
