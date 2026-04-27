package com.example.Inventory.dto;

import lombok.Builder;

@Builder
public record LowOnStockDTO(
        String name,
        Float price,
        Integer quantity
) {
}
