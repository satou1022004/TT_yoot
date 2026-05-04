package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.OrderDto;
import com.example.de_tai_tt.fooddelivery.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(target = "voucherId", source = "voucher.id")
    @Mapping(target = "voucherCode", source = "voucher.code")
    @Mapping(target = "deliveryAgentId", source = "deliveryAgent.id")
    @Mapping(target = "deliveryAgentName", source = "deliveryAgent.fullName")
    OrderDto toDto(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "voucher.id", source = "voucherId")
    @Mapping(target = "deliveryAgent.id", source = "deliveryAgentId")
    Order toEntity(OrderDto dto);
}
