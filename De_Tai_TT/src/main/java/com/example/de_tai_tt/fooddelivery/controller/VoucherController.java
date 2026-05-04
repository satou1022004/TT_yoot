package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.VoucherValidationRequest;
import com.example.de_tai_tt.fooddelivery.dto.VoucherValidationResponse;
import com.example.de_tai_tt.fooddelivery.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/validate")
    public VoucherValidationResponse validate(@Valid @RequestBody VoucherValidationRequest request) {
        return voucherService.validate(request);
    }
}
