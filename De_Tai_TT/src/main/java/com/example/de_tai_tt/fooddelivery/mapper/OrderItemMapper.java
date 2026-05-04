package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.OrderItemDto;
import com.example.de_tai_tt.fooddelivery.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "menuItemId", source = "menuItem.id")
    OrderItemDto toDto(OrderItem item);

    @Mapping(target = "menuItem.id", source = "menuItemId")
    OrderItem toEntity(OrderItemDto dto);
}
