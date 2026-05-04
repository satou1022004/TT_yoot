package com.example.de_tai_tt.fooddelivery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty List<@Valid CreateOrderItemRequest> items,
        @Size(max = 40) String voucherCode,
        @NotBlank @Size(max = 255) String deliveryAddress,
        @Size(max = 500) String note
) {
}
