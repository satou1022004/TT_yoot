package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.DeliveryAgentDto;
import com.example.de_tai_tt.fooddelivery.dto.DeliveryAgentRequest;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface DeliveryAgentMapper {
    DeliveryAgentDto toDto(DeliveryAgent deliveryAgent);

    DeliveryAgent toEntity(DeliveryAgentDto dto);

    DeliveryAgent toEntity(DeliveryAgentRequest request);

    void update(DeliveryAgentRequest request, @MappingTarget DeliveryAgent deliveryAgent);
}
