package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.RestaurantDto;
import com.example.de_tai_tt.fooddelivery.dto.RestaurantRequest;
import com.example.de_tai_tt.fooddelivery.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface RestaurantMapper {
    RestaurantDto toDto(Restaurant restaurant);

    Restaurant toEntity(RestaurantDto dto);

    Restaurant toEntity(RestaurantRequest request);

    void update(RestaurantRequest request, @MappingTarget Restaurant restaurant);
}
