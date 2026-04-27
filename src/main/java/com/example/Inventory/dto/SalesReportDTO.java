package com.example.Inventory.dto;

import lombok.Builder;

@Builder
public record SalesReportDTO(
        String name,
        Float price,
        Integer quantity
) {
}
