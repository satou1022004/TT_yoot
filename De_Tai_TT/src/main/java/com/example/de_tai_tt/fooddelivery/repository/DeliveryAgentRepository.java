package com.example.de_tai_tt.fooddelivery.repository;

import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgent;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    List<DeliveryAgent> findByStatus(DeliveryAgentStatus status);
}
