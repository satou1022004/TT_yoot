package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.Role;

import java.util.Set;

public record UserDto(
        Long id,
        String username,
        String email,
        String fullName,
        String phone,
        String address,
        boolean enabled,
        Set<Role> roles
) {
}
