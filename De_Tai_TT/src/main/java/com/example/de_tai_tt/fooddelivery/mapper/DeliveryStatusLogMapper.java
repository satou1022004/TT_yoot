package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.DeliveryStatusLogDto;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryStatusLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface DeliveryStatusLogMapper {
    @Mapping(target = "orderId", source = "order.id")
    DeliveryStatusLogDto toDto(DeliveryStatusLog log);

    @Mapping(target = "order.id", source = "orderId")
    DeliveryStatusLog toEntity(DeliveryStatusLogDto dto);
}
