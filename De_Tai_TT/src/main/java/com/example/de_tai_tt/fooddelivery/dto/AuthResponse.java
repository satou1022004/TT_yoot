package com.example.de_tai_tt.fooddelivery.dto;

public record AuthResponse(
        String token,
        String tokenType,
        UserDto user
) {
}
