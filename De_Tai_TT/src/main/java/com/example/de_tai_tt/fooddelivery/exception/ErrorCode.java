package com.example.de_tai_tt.fooddelivery.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(HttpStatus.FORBIDDEN),
    VOUCHER_INVALID(HttpStatus.BAD_REQUEST),
    ORDER_RESTAURANT_MISMATCH(HttpStatus.BAD_REQUEST),
    ORDER_STATUS_TRANSITION_INVALID(HttpStatus.BAD_REQUEST),
    MENU_ITEM_UNAVAILABLE(HttpStatus.BAD_REQUEST),
    DELIVERY_AGENT_UNAVAILABLE(HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus status() {
        return status;
    }
}
