package com.example.de_tai_tt.fooddelivery.repository;

import com.example.de_tai_tt.fooddelivery.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurantIdAndAvailableTrueOrderByNameAsc(Long restaurantId);

    List<MenuItem> findByRestaurantIdOrderByNameAsc(Long restaurantId);
}
