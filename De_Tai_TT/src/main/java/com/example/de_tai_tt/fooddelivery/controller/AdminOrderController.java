package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.DashboardSummaryDto;
import com.example.de_tai_tt.fooddelivery.dto.OrderDto;
import com.example.de_tai_tt.fooddelivery.dto.UpdateOrderStatusRequest;
import com.example.de_tai_tt.fooddelivery.service.DashboardService;
import com.example.de_tai_tt.fooddelivery.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final DashboardService dashboardService;

    @GetMapping("/orders/{id}")
    public OrderDto getOrder(@PathVariable @Positive Long id) {
        return orderService.getForAdmin(id);
    }

    @PatchMapping("/orders/{id}/status")
    public OrderDto updateStatus(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return orderService.updateStatus(id, request);
    }

    @GetMapping("/dashboard")
    public DashboardSummaryDto dashboard() {
        return dashboardService.summary();
    }
}
