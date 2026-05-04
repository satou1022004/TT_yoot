package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.MenuItemDto;
import com.example.de_tai_tt.fooddelivery.dto.RestaurantDto;
import com.example.de_tai_tt.fooddelivery.service.RestaurantService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDto> findRestaurants() {
        return restaurantService.findPublicRestaurants();
    }

    @GetMapping("/{id}")
    public RestaurantDto getRestaurant(@PathVariable @Positive Long id) {
        return restaurantService.get(id);
    }

    @GetMapping("/{id}/menu")
    public List<MenuItemDto> getMenu(@PathVariable @Positive Long id) {
        return restaurantService.publicMenu(id);
    }
}
