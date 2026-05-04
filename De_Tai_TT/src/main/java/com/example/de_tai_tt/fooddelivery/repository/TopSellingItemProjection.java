package com.example.de_tai_tt.fooddelivery.repository;

import java.math.BigDecimal;

public interface TopSellingItemProjection {
    String getItemName();

    Long getQuantitySold();

    BigDecimal getGrossSales();
}
