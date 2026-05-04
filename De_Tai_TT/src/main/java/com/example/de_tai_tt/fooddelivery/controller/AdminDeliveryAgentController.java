package com.example.de_tai_tt.fooddelivery.controller;

import com.example.de_tai_tt.fooddelivery.dto.DeliveryAgentDto;
import com.example.de_tai_tt.fooddelivery.dto.DeliveryAgentRequest;
import com.example.de_tai_tt.fooddelivery.service.DeliveryAgentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/delivery-agents")
@RequiredArgsConstructor
public class AdminDeliveryAgentController {

    private final DeliveryAgentService deliveryAgentService;

    @GetMapping
    public List<DeliveryAgentDto> findAll() {
        return deliveryAgentService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryAgentDto create(@Valid @RequestBody DeliveryAgentRequest request) {
        return deliveryAgentService.create(request);
    }

    @PutMapping("/{id}")
    public DeliveryAgentDto update(@PathVariable @Positive Long id, @Valid @RequestBody DeliveryAgentRequest request) {
        return deliveryAgentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        deliveryAgentService.delete(id);
    }
}
