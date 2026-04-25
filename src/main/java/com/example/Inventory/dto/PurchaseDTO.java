package com.example.Inventory.dto;

import lombok.Builder;

@Builder
public record PurchaseDTO(
        int productId,
        String product,
        float price,
        int quantity
) {
}
