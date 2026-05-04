package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.MenuItemDto;
import com.example.de_tai_tt.fooddelivery.dto.MenuItemRequest;
import com.example.de_tai_tt.fooddelivery.service.MenuItemService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/menu-items")
@RequiredArgsConstructor
public class AdminMenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping
    public List<MenuItemDto> findByRestaurant(@RequestParam @Positive Long restaurantId) {
        return menuItemService.findAllByRestaurant(restaurantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemDto create(@Valid @RequestBody MenuItemRequest request) {
        return menuItemService.create(request);
    }

    @PutMapping("/{id}")
    public MenuItemDto update(@PathVariable @Positive Long id, @Valid @RequestBody MenuItemRequest request) {
        return menuItemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        menuItemService.delete(id);
    }
}
