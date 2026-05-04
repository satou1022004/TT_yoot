package com.example.de_tai_tt.fooddelivery.repository;

import com.example.de_tai_tt.fooddelivery.entity.Order;
import com.example.de_tai_tt.fooddelivery.entity.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"restaurant", "voucher", "deliveryAgent", "items", "items.menuItem"})
    Optional<Order> findWithItemsById(Long id);

    @EntityGraph(attributePaths = {"restaurant", "voucher", "deliveryAgent", "items", "items.menuItem"})
    List<Order> findByUserUsernameOrderByCreatedAtDesc(String username);

    long countByStatus(OrderStatus status);

    @Query("""
            select o.status as status, count(o.id) as total
            from FoodOrder o
            group by o.status
            """)
    List<OrderStatusCountProjection> countGroupedByStatus();

    @Query(value = """
            select cast(o.created_at as date) as date, coalesce(sum(o.total_amount), 0) as revenue
            from orders o
            where o.status = 'COMPLETED'
              and o.created_at >= :from
            group by cast(o.created_at as date)
            order by date
            """, nativeQuery = true)
    List<RevenueByDateProjection> revenueByDate(@Param("from") Instant from);

    @Query("""
            select i.itemName as itemName,
                   sum(i.quantity) as quantitySold,
                   sum(i.lineTotal) as grossSales
            from OrderItem i
            where i.order.status = com.example.de_tai_tt.fooddelivery.entity.OrderStatus.COMPLETED
            group by i.itemName
            order by sum(i.quantity) desc
            """)
    List<TopSellingItemProjection> topSellingItems(Pageable pageable);
}
