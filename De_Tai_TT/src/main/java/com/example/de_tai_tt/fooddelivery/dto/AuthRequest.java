package com.example.de_tai_tt.fooddelivery.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
