package com.example.de_tai_tt.fooddelivery.dto;

import java.math.BigDecimal;

public record VoucherValidationResponse(
        String code,
        boolean valid,
        BigDecimal discountAmount,
        String message
) {
}
