package com.example.de_tai_tt.fooddelivery.service;

import com.example.de_tai_tt.fooddelivery.dto.CreateOrderItemRequest;
import com.example.de_tai_tt.fooddelivery.dto.CreateOrderRequest;
import com.example.de_tai_tt.fooddelivery.dto.DeliveryStatusLogDto;
import com.example.de_tai_tt.fooddelivery.dto.OrderDto;
import com.example.de_tai_tt.fooddelivery.dto.UpdateOrderStatusRequest;
import com.example.de_tai_tt.fooddelivery.entity.AppUser;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgent;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryStatusLog;
import com.example.de_tai_tt.fooddelivery.entity.MenuItem;
import com.example.de_tai_tt.fooddelivery.entity.Order;
import com.example.de_tai_tt.fooddelivery.entity.OrderItem;
import com.example.de_tai_tt.fooddelivery.entity.OrderStatus;
import com.example.de_tai_tt.fooddelivery.entity.Restaurant;
import com.example.de_tai_tt.fooddelivery.entity.Voucher;
import com.example.de_tai_tt.fooddelivery.exception.BusinessException;
import com.example.de_tai_tt.fooddelivery.exception.ErrorCode;
import com.example.de_tai_tt.fooddelivery.exception.ResourceNotFoundException;
import com.example.de_tai_tt.fooddelivery.mapper.DeliveryStatusLogMapper;
import com.example.de_tai_tt.fooddelivery.mapper.OrderMapper;
import com.example.de_tai_tt.fooddelivery.repository.DeliveryStatusLogRepository;
import com.example.de_tai_tt.fooddelivery.repository.MenuItemRepository;
import com.example.de_tai_tt.fooddelivery.repository.OrderRepository;
import com.example.de_tai_tt.fooddelivery.repository.UserRepository;
import com.example.de_tai_tt.fooddelivery.security.SecurityUtil;
import com.example.de_tai_tt.fooddelivery.util.DistanceCalculator;
import com.example.de_tai_tt.fooddelivery.util.MoneyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final BigDecimal BASE_DELIVERY_FEE = BigDecimal.valueOf(15000);
    private static final BigDecimal PER_KM_FEE = BigDecimal.valueOf(3500);

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final DeliveryStatusLogRepository statusLogRepository;
    private final VoucherService voucherService;
    private final DeliveryAgentService deliveryAgentService;
    private final OrderMapper orderMapper;
    private final DeliveryStatusLogMapper statusLogMapper;

    private final Map<OrderStatus, OrderStatus> allowedNextStatuses = new EnumMap<>(Map.of(
            OrderStatus.PENDING, OrderStatus.PREPARING,
            OrderStatus.PREPARING, OrderStatus.DELIVERING,
            OrderStatus.DELIVERING, OrderStatus.COMPLETED
    ));

    @Transactional
    public OrderDto create(CreateOrderRequest request) {
        AppUser user = userRepository.findByUsername(SecurityUtil.currentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", SecurityUtil.currentUsername()));

        List<MenuItem> menuItems = request.items().stream()
                .map(item -> menuItemRepository.findById(item.menuItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Menu item", item.menuItemId())))
                .toList();

        Restaurant restaurant = validateSingleRestaurantAndAvailability(menuItems);
        BigDecimal subtotal = calculateSubtotal(request.items(), menuItems);

        Voucher voucher = null;
        BigDecimal discount = BigDecimal.ZERO;
        if (StringUtils.hasText(request.voucherCode())) {
            voucher = voucherService.findByCode(request.voucherCode());
            discount = voucherService.validateAndCalculate(voucher, subtotal);
            voucherService.markUsed(voucher);
        }

        DeliveryAgent agent = deliveryAgentService.findNearestAvailable(restaurant.getLatitude(), restaurant.getLongitude());
        BigDecimal deliveryFee = calculateDeliveryFee(restaurant, agent);
        BigDecimal total = MoneyUtil.money(subtotal.add(deliveryFee).subtract(discount));

        Order order = Order.builder()
                .user(user)
                .restaurant(restaurant)
                .voucher(voucher)
                .deliveryAgent(agent)
                .status(OrderStatus.PENDING)
                .subtotal(subtotal)
                .deliveryFee(deliveryFee)
                .discountAmount(discount)
                .totalAmount(total)
                .deliveryAddress(request.deliveryAddress())
                .note(request.note())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        for (CreateOrderItemRequest itemRequest : request.items()) {
            MenuItem item = menuItems.stream()
                    .filter(menuItem -> menuItem.getId().equals(itemRequest.menuItemId()))
                    .findFirst()
                    .orElseThrow();
            BigDecimal lineTotal = MoneyUtil.money(item.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            order.addItem(OrderItem.builder()
                    .menuItem(item)
                    .itemName(item.getName())
                    .unitPrice(item.getPrice())
                    .quantity(itemRequest.quantity())
                    .lineTotal(lineTotal)
                    .build());
        }

        Order saved = orderRepository.save(order);
        writeStatusLog(saved, OrderStatus.PENDING, user.getUsername(), "Order created");
        return orderMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> myOrders() {
        return orderRepository.findByUserUsernameOrderByCreatedAtDesc(SecurityUtil.currentUsername()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDto getForCurrentUser(Long id) {
        Order order = findWithItems(id);
        if (!order.getUser().getUsername().equals(SecurityUtil.currentUsername())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Order does not belong to current user");
        }
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderDto getForAdmin(Long id) {
        return orderMapper.toDto(findWithItems(id));
    }

    @Transactional
    public OrderDto updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = findWithItems(id);
        OrderStatus expected = allowedNextStatuses.get(order.getStatus());
        if (expected == null || expected != request.status()) {
            throw new BusinessException(
                    ErrorCode.ORDER_STATUS_TRANSITION_INVALID,
                    "Invalid status transition from " + order.getStatus() + " to " + request.status()
            );
        }
        order.setStatus(request.status());
        order.setUpdatedAt(Instant.now());
        writeStatusLog(order, request.status(), SecurityUtil.currentUsername(), request.note());
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<DeliveryStatusLogDto> logs(Long orderId) {
        findWithItems(orderId);
        return statusLogRepository.findByOrderIdOrderByCreatedAtAsc(orderId).stream()
                .map(statusLogMapper::toDto)
                .toList();
    }

    private Restaurant validateSingleRestaurantAndAvailability(List<MenuItem> menuItems) {
        if (menuItems.isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Order must contain at least one item");
        }
        Restaurant restaurant = menuItems.getFirst().getRestaurant();
        for (MenuItem item : menuItems) {
            if (!item.isAvailable()) {
                throw new BusinessException(ErrorCode.MENU_ITEM_UNAVAILABLE, "Menu item is not available: " + item.getName());
            }
            if (!item.getRestaurant().getId().equals(restaurant.getId())) {
                throw new BusinessException(ErrorCode.ORDER_RESTAURANT_MISMATCH, "An order can contain items from only one restaurant");
            }
        }
        return restaurant;
    }

    private BigDecimal calculateSubtotal(List<CreateOrderItemRequest> requestedItems, List<MenuItem> menuItems) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CreateOrderItemRequest request : requestedItems) {
            MenuItem menuItem = menuItems.stream()
                    .filter(item -> item.getId().equals(request.menuItemId()))
                    .findFirst()
                    .orElseThrow();
            subtotal = subtotal.add(menuItem.getPrice().multiply(BigDecimal.valueOf(request.quantity())));
        }
        return MoneyUtil.money(subtotal);
    }

    private BigDecimal calculateDeliveryFee(Restaurant restaurant, DeliveryAgent agent) {
        double distance = DistanceCalculator.distanceKm(
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                agent.getCurrentLatitude(),
                agent.getCurrentLongitude()
        );
        return MoneyUtil.money(BASE_DELIVERY_FEE.add(PER_KM_FEE.multiply(BigDecimal.valueOf(distance))));
    }

    private void writeStatusLog(Order order, OrderStatus status, String changedBy, String note) {
        statusLogRepository.save(DeliveryStatusLog.builder()
                .order(order)
                .status(status)
                .changedBy(changedBy)
                .note(note)
                .createdAt(Instant.now())
                .build());
    }

    private Order findWithItems(Long id) {
        return orderRepository.findWithItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }
}
