package com.example.de_tai_tt.fooddelivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull Long menuItemId,
        @NotNull @Min(1) Integer quantity
) {
}
