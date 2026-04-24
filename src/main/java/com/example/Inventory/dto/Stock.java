package com.example.Inventory.dto;

public record Stock(
        int productId,
        int currentQuantity
) {
}
