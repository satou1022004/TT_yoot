package com.example.de_tai_tt.fooddelivery.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String errorCode,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}
