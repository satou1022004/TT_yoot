package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.CreateOrderRequest;
import com.example.de_tai_tt.fooddelivery.dto.DeliveryStatusLogDto;
import com.example.de_tai_tt.fooddelivery.dto.OrderDto;
import com.example.de_tai_tt.fooddelivery.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping
    public List<OrderDto> history() {
        return orderService.myOrders();
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable @Positive Long id) {
        return orderService.getForCurrentUser(id);
    }

    @GetMapping("/{id}/logs")
    public List<DeliveryStatusLogDto> logs(@PathVariable @Positive Long id) {
        return orderService.logs(id);
    }
}
