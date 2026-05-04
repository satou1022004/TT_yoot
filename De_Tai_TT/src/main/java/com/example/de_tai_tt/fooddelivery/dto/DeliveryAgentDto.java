package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgentStatus;

public record DeliveryAgentDto(
        Long id,
        String fullName,
        String phone,
        Double currentLatitude,
        Double currentLongitude,
        DeliveryAgentStatus status
) {
}
