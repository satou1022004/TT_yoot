package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VoucherDto(
        Long id,
        String code,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minOrderAmount,
        LocalDate expiresAt,
        Integer usageLimit,
        Integer usedCount,
        boolean active
) {
}
