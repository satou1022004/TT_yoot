package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeliveryAgentRequest(
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Size(max = 30) String phone,
        @NotNull Double currentLatitude,
        @NotNull Double currentLongitude,
        @NotNull DeliveryAgentStatus status
) {
}
