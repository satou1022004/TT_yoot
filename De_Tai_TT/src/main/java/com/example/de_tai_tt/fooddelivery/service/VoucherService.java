package com.example.de_tai_tt.fooddelivery.service;

import com.example.de_tai_tt.fooddelivery.dto.VoucherDto;
import com.example.de_tai_tt.fooddelivery.dto.VoucherRequest;
import com.example.de_tai_tt.fooddelivery.dto.VoucherValidationRequest;
import com.example.de_tai_tt.fooddelivery.dto.VoucherValidationResponse;
import com.example.de_tai_tt.fooddelivery.entity.DiscountType;
import com.example.de_tai_tt.fooddelivery.entity.Voucher;
import com.example.de_tai_tt.fooddelivery.exception.BusinessException;
import com.example.de_tai_tt.fooddelivery.exception.ErrorCode;
import com.example.de_tai_tt.fooddelivery.exception.ResourceNotFoundException;
import com.example.de_tai_tt.fooddelivery.mapper.VoucherMapper;
import com.example.de_tai_tt.fooddelivery.repository.VoucherRepository;
import com.example.de_tai_tt.fooddelivery.util.MoneyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    @Transactional(readOnly = true)
    public List<VoucherDto> findAll() {
        return voucherRepository.findAll().stream()
                .map(voucherMapper::toDto)
                .toList();
    }

    @Transactional
    public VoucherDto create(VoucherRequest request) {
        if (voucherRepository.existsByCodeIgnoreCase(request.code())) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "Voucher code already exists");
        }
        Voucher voucher = voucherMapper.toEntity(request);
        voucher.setCode(request.code().toUpperCase());
        voucher.setUsedCount(0);
        voucher.setActive(request.active() == null || request.active());
        return voucherMapper.toDto(voucherRepository.save(voucher));
    }

    @Transactional
    public VoucherDto update(Long id, VoucherRequest request) {
        Voucher voucher = findEntity(id);
        if (!voucher.getCode().equalsIgnoreCase(request.code()) && voucherRepository.existsByCodeIgnoreCase(request.code())) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "Voucher code already exists");
        }
        voucherMapper.update(request, voucher);
        voucher.setCode(request.code().toUpperCase());
        if (request.active() != null) {
            voucher.setActive(request.active());
        }
        return voucherMapper.toDto(voucher);
    }

    @Transactional
    public void delete(Long id) {
        voucherRepository.delete(findEntity(id));
    }

    @Transactional(readOnly = true)
    public VoucherValidationResponse validate(VoucherValidationRequest request) {
        Voucher voucher = findByCode(request.code());
        BigDecimal discount = validateAndCalculate(voucher, request.orderAmount());
        return new VoucherValidationResponse(voucher.getCode(), true, discount, "Voucher is valid");
    }

    Voucher findByCode(String code) {
        return voucherRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new BusinessException(ErrorCode.VOUCHER_INVALID, "Voucher code is invalid"));
    }

    Voucher findEntity(Long id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher", id));
    }

    BigDecimal validateAndCalculate(Voucher voucher, BigDecimal orderAmount) {
        if (!voucher.isActive()) {
            throw new BusinessException(ErrorCode.VOUCHER_INVALID, "Voucher is inactive");
        }
        if (voucher.getExpiresAt().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.VOUCHER_INVALID, "Voucher has expired");
        }
        if (voucher.getUsedCount() >= voucher.getUsageLimit()) {
            throw new BusinessException(ErrorCode.VOUCHER_INVALID, "Voucher usage limit has been reached");
        }
        if (orderAmount.compareTo(voucher.getMinOrderAmount()) < 0) {
            throw new BusinessException(ErrorCode.VOUCHER_INVALID, "Order amount does not meet voucher minimum");
        }
        BigDecimal discount = voucher.getDiscountType() == DiscountType.PERCENTAGE
                ? orderAmount.multiply(voucher.getDiscountValue()).divide(ONE_HUNDRED)
                : voucher.getDiscountValue();
        return MoneyUtil.money(discount.min(orderAmount));
    }

    void markUsed(Voucher voucher) {
        voucher.setUsedCount(voucher.getUsedCount() + 1);
    }
}
