package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(
        Long id,
        Long userId,
        Long restaurantId,
        String restaurantName,
        Long voucherId,
        String voucherCode,
        Long deliveryAgentId,
        String deliveryAgentName,
        OrderStatus status,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        String deliveryAddress,
        String note,
        Instant createdAt,
        Instant updatedAt,
        List<OrderItemDto> items
) {
}
