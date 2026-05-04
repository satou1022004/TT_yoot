package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.OrderStatus;

import java.time.Instant;

public record DeliveryStatusLogDto(
        Long id,
        Long orderId,
        OrderStatus status,
        String changedBy,
        String note,
        Instant createdAt
) {
}
