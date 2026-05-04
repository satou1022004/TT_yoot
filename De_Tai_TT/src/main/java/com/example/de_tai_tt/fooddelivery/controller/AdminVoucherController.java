package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.VoucherDto;
import com.example.de_tai_tt.fooddelivery.dto.VoucherRequest;
import com.example.de_tai_tt.fooddelivery.service.VoucherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/vouchers")
@RequiredArgsConstructor
public class AdminVoucherController {

    private final VoucherService voucherService;

    @GetMapping
    public List<VoucherDto> findAll() {
        return voucherService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherDto create(@Valid @RequestBody VoucherRequest request) {
        return voucherService.create(request);
    }

    @PutMapping("/{id}")
    public VoucherDto update(@PathVariable @Positive Long id, @Valid @RequestBody VoucherRequest request) {
        return voucherService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        voucherService.delete(id);
    }
}
