package com.example.de_tai_tt.fooddelivery.mapper;

import com.example.de_tai_tt.fooddelivery.dto.UserDto;
import com.example.de_tai_tt.fooddelivery.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {
    UserDto toDto(AppUser user);

    AppUser toEntity(UserDto dto);
}
