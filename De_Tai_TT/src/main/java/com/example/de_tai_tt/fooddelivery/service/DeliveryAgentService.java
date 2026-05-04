package com.example.de_tai_tt.fooddelivery.service;

import com.example.de_tai_tt.fooddelivery.dto.DeliveryAgentDto;
import com.example.de_tai_tt.fooddelivery.dto.DeliveryAgentRequest;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgent;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgentStatus;
import com.example.de_tai_tt.fooddelivery.exception.BusinessException;
import com.example.de_tai_tt.fooddelivery.exception.ErrorCode;
import com.example.de_tai_tt.fooddelivery.exception.ResourceNotFoundException;
import com.example.de_tai_tt.fooddelivery.mapper.DeliveryAgentMapper;
import com.example.de_tai_tt.fooddelivery.repository.DeliveryAgentRepository;
import com.example.de_tai_tt.fooddelivery.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAgentService {

    private final DeliveryAgentRepository deliveryAgentRepository;
    private final DeliveryAgentMapper deliveryAgentMapper;

    @Transactional(readOnly = true)
    public List<DeliveryAgentDto> findAll() {
        return deliveryAgentRepository.findAll().stream()
                .map(deliveryAgentMapper::toDto)
                .toList();
    }

    @Transactional
    public DeliveryAgentDto create(DeliveryAgentRequest request) {
        return deliveryAgentMapper.toDto(deliveryAgentRepository.save(deliveryAgentMapper.toEntity(request)));
    }

    @Transactional
    public DeliveryAgentDto update(Long id, DeliveryAgentRequest request) {
        DeliveryAgent agent = findEntity(id);
        deliveryAgentMapper.update(request, agent);
        return deliveryAgentMapper.toDto(agent);
    }

    @Transactional
    public void delete(Long id) {
        deliveryAgentRepository.delete(findEntity(id));
    }

    @Transactional(readOnly = true)
    public DeliveryAgent findNearestAvailable(double latitude, double longitude) {
        return deliveryAgentRepository.findByStatus(DeliveryAgentStatus.AVAILABLE).stream()
                .min(Comparator.comparingDouble(agent -> DistanceCalculator.distanceKm(
                        latitude,
                        longitude,
                        agent.getCurrentLatitude(),
                        agent.getCurrentLongitude())))
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_AGENT_UNAVAILABLE, "No available delivery agent"));
    }

    DeliveryAgent findEntity(Long id) {
        return deliveryAgentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery agent", id));
    }
}
