package com.example.de_tai_tt.fooddelivery.service;

import com.example.de_tai_tt.fooddelivery.dto.MenuItemDto;
import com.example.de_tai_tt.fooddelivery.dto.MenuItemRequest;
import com.example.de_tai_tt.fooddelivery.entity.MenuItem;
import com.example.de_tai_tt.fooddelivery.entity.Restaurant;
import com.example.de_tai_tt.fooddelivery.exception.ResourceNotFoundException;
import com.example.de_tai_tt.fooddelivery.mapper.MenuItemMapper;
import com.example.de_tai_tt.fooddelivery.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;
    private final MenuItemMapper menuItemMapper;

    @Transactional(readOnly = true)
    public List<MenuItemDto> findAllByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdOrderByNameAsc(restaurantId).stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Transactional
    public MenuItemDto create(MenuItemRequest request) {
        Restaurant restaurant = restaurantService.findEntity(request.restaurantId());
        MenuItem item = menuItemMapper.toEntity(request);
        item.setRestaurant(restaurant);
        item.setAvailable(request.available() == null || request.available());
        return menuItemMapper.toDto(menuItemRepository.save(item));
    }

    @Transactional
    public MenuItemDto update(Long id, MenuItemRequest request) {
        MenuItem item = findEntity(id);
        Restaurant restaurant = restaurantService.findEntity(request.restaurantId());
        menuItemMapper.update(request, item);
        item.setRestaurant(restaurant);
        if (request.available() != null) {
            item.setAvailable(request.available());
        }
        return menuItemMapper.toDto(item);
    }

    @Transactional
    public void delete(Long id) {
        menuItemRepository.delete(findEntity(id));
    }

    MenuItem findEntity(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item", id));
    }
}
