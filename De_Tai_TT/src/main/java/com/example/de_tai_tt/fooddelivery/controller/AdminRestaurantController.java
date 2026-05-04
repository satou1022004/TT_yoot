package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.RestaurantDto;
import com.example.de_tai_tt.fooddelivery.dto.RestaurantRequest;
import com.example.de_tai_tt.fooddelivery.service.RestaurantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDto> findAll() {
        return restaurantService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDto create(@Valid @RequestBody RestaurantRequest request) {
        return restaurantService.create(request);
    }

    @PutMapping("/{id}")
    public RestaurantDto update(@PathVariable @Positive Long id, @Valid @RequestBody RestaurantRequest request) {
        return restaurantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        restaurantService.delete(id);
    }
}
