package com.example.Inventory.dto;

import lombok.Builder;

@Builder
public record ProductSummaryDTO(
        String name,
        Integer quantity
) {
}
