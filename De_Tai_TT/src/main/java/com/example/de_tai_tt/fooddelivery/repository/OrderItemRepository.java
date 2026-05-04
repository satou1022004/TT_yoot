package com.example.de_tai_tt.fooddelivery.repository;

import com.example.de_tai_tt.fooddelivery.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
