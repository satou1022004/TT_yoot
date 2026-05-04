package com.example.de_tai_tt.fooddelivery.service;

import com.example.de_tai_tt.fooddelivery.dto.MenuItemDto;
import com.example.de_tai_tt.fooddelivery.dto.RestaurantDto;
import com.example.de_tai_tt.fooddelivery.dto.RestaurantRequest;
import com.example.de_tai_tt.fooddelivery.entity.Restaurant;
import com.example.de_tai_tt.fooddelivery.exception.ResourceNotFoundException;
import com.example.de_tai_tt.fooddelivery.mapper.MenuItemMapper;
import com.example.de_tai_tt.fooddelivery.mapper.RestaurantMapper;
import com.example.de_tai_tt.fooddelivery.repository.MenuItemRepository;
import com.example.de_tai_tt.fooddelivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;

    @Transactional(readOnly = true)
    public List<RestaurantDto> findPublicRestaurants() {
        return restaurantRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RestaurantDto get(Long id) {
        return restaurantMapper.toDto(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<MenuItemDto> publicMenu(Long restaurantId) {
        findEntity(restaurantId);
        return menuItemRepository.findByRestaurantIdAndAvailableTrueOrderByNameAsc(restaurantId).stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Transactional
    public RestaurantDto create(RestaurantRequest request) {
        Restaurant restaurant = restaurantMapper.toEntity(request);
        restaurant.setActive(request.active() == null || request.active());
        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantDto update(Long id, RestaurantRequest request) {
        Restaurant restaurant = findEntity(id);
        restaurantMapper.update(request, restaurant);
        if (request.active() != null) {
            restaurant.setActive(request.active());
        }
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional
    public void delete(Long id) {
        restaurantRepository.delete(findEntity(id));
    }

    Restaurant findEntity(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
    }
}
