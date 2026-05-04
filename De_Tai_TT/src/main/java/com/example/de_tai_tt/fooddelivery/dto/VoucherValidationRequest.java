package com.example.de_tai_tt.fooddelivery.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VoucherValidationRequest(
        @NotBlank String code,
        @NotNull @DecimalMin("0.00") BigDecimal orderAmount
) {
}
