package com.example.de_tai_tt.fooddelivery.dto;

import java.math.BigDecimal;

public record MenuItemDto(
        Long id,
        Long restaurantId,
        String name,
        String description,
        BigDecimal price,
        boolean available
) {
}
