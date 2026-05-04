package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.VoucherDto;
import com.example.de_tai_tt.fooddelivery.dto.VoucherRequest;
import com.example.de_tai_tt.fooddelivery.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface VoucherMapper {
    VoucherDto toDto(Voucher voucher);

    Voucher toEntity(VoucherDto dto);

    Voucher toEntity(VoucherRequest request);

    void update(VoucherRequest request, @MappingTarget Voucher voucher);
}
