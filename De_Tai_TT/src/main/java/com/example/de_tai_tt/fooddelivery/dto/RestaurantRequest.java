package com.example.de_tai_tt.fooddelivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(
        @NotBlank @Size(max = 160) String name,
        @NotBlank @Size(max = 100) String cuisineType,
        @NotBlank @Size(max = 255) String address,
        @NotNull Double latitude,
        @NotNull Double longitude,
        Boolean active
) {
}
