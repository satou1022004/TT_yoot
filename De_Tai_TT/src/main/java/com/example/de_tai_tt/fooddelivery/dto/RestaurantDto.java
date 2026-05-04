package com.example.de_tai_tt.fooddelivery.dto;

public record RestaurantDto(
        Long id,
        String name,
        String cuisineType,
        String address,
        Double latitude,
        Double longitude,
        boolean active
) {
}
