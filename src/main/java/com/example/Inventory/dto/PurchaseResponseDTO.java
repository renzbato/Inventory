package com.example.Inventory.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PurchaseResponseDTO(
        List<PurchaseDTO> purchase,
        float totalPurchase
) {
}
