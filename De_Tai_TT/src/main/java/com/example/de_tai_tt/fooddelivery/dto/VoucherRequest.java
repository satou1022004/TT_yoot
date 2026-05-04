package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VoucherRequest(
        @NotBlank @Size(max = 40) String code,
        @NotNull DiscountType discountType,
        @NotNull @DecimalMin("0.01") BigDecimal discountValue,
        @NotNull @DecimalMin("0.00") BigDecimal minOrderAmount,
        @NotNull @FutureOrPresent LocalDate expiresAt,
        @NotNull @Min(1) Integer usageLimit,
        Boolean active
) {
}
