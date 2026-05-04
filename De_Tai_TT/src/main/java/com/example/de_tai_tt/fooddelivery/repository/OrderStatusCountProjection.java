package com.example.de_tai_tt.fooddelivery.repository;

import com.example.de_tai_tt.fooddelivery.entity.OrderStatus;

public interface OrderStatusCountProjection {
    OrderStatus getStatus();

    Long getTotal();
}
