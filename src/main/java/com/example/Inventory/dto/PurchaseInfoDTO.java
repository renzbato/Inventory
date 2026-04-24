package com.example.Inventory.dto;

import lombok.Builder;

@Builder
public record PurchaseInfoDTO(
        String product,
        float price,
        int quantity
) {
}
