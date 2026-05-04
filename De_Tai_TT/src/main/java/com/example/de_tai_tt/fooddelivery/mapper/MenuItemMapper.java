package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.MenuItemDto;
import com.example.de_tai_tt.fooddelivery.dto.MenuItemRequest;
import com.example.de_tai_tt.fooddelivery.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface MenuItemMapper {
    @Mapping(target = "restaurantId", source = "restaurant.id")
    MenuItemDto toDto(MenuItem menuItem);

    @Mapping(target = "restaurant.id", source = "restaurantId")
    MenuItem toEntity(MenuItemDto dto);

    @Mapping(target = "restaurant", ignore = true)
    MenuItem toEntity(MenuItemRequest request);

    @Mapping(target = "restaurant", ignore = true)
    void update(MenuItemRequest request, @MappingTarget MenuItem menuItem);
}
