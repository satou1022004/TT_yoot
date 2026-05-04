package com.example.de_tai_tt.fooddelivery.repository;

import com.example.de_tai_tt.fooddelivery.entity.DeliveryStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryStatusLogRepository extends JpaRepository<DeliveryStatusLog, Long> {
    List<DeliveryStatusLog> findByOrderIdOrderByCreatedAtAsc(Long orderId);
}
