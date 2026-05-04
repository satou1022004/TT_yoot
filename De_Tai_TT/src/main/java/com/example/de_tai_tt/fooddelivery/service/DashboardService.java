package com.example.de_tai_tt.fooddelivery.service;

import com.example.de_tai_tt.fooddelivery.dto.DashboardSummaryDto;
import com.example.de_tai_tt.fooddelivery.entity.OrderStatus;
import com.example.de_tai_tt.fooddelivery.repository.OrderRepository;
import com.example.de_tai_tt.fooddelivery.repository.OrderStatusCountProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDto summary() {
        Map<OrderStatus, Long> counts = new EnumMap<>(OrderStatus.class);
        Arrays.stream(OrderStatus.values()).forEach(status -> counts.put(status, 0L));
        for (OrderStatusCountProjection projection : orderRepository.countGroupedByStatus()) {
            counts.put(projection.getStatus(), projection.getTotal());
        }

        List<DashboardSummaryDto.RevenueByDateDto> revenue = orderRepository.revenueByDate(Instant.now().minus(30, ChronoUnit.DAYS))
                .stream()
                .map(row -> new DashboardSummaryDto.RevenueByDateDto(row.getDate(), row.getRevenue()))
                .toList();

        List<DashboardSummaryDto.TopSellingItemDto> topItems = orderRepository.topSellingItems(PageRequest.of(0, 10))
                .stream()
                .map(row -> new DashboardSummaryDto.TopSellingItemDto(row.getItemName(), row.getQuantitySold(), row.getGrossSales()))
                .toList();

        return new DashboardSummaryDto(counts, revenue, topItems);
    }
}
