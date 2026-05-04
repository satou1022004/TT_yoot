package com.example.de_tai_tt.fooddelivery.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long menuItemId,
        String itemName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal lineTotal
) {
}
