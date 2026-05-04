package com.example.de_tai_tt.fooddelivery.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RevenueByDateProjection {
    LocalDate getDate();

    BigDecimal getRevenue();
}
