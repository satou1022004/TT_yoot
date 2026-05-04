package com.example.de_tai_tt.fooddelivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 80) String username,
        @NotBlank @Email @Size(max = 160) String email,
        @NotBlank @Size(min = 6, max = 80) String password,
        @NotBlank @Size(max = 120) String fullName,
        @Size(max = 30) String phone,
        @Size(max = 255) String address
) {
}
