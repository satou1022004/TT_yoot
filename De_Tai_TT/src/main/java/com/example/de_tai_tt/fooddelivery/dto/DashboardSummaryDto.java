package com.example.de_tai_tt.fooddelivery.dto;

import com.example.de_tai_tt.fooddelivery.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record DashboardSummaryDto(
        Map<OrderStatus, Long> ordersByStatus,
        List<RevenueByDateDto> revenueByDate,
        List<TopSellingItemDto> topSellingItems
) {

    public record RevenueByDateDto(LocalDate date, BigDecimal revenue) {
    }

    public record TopSellingItemDto(String itemName, Long quantitySold, BigDecimal grossSales) {
    }
}
