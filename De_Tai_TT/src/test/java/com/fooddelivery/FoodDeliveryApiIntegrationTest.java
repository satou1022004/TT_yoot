package com.example.de_tai_tt.fooddelivery;

import com.example.de_tai_tt.fooddelivery.entity.AppUser;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgent;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgentStatus;
import com.example.de_tai_tt.fooddelivery.entity.DiscountType;
import com.example.de_tai_tt.fooddelivery.entity.MenuItem;
import com.example.de_tai_tt.fooddelivery.entity.Restaurant;
import com.example.de_tai_tt.fooddelivery.entity.Role;
import com.example.de_tai_tt.fooddelivery.entity.Voucher;
import com.example.de_tai_tt.fooddelivery.repository.DeliveryAgentRepository;
import com.example.de_tai_tt.fooddelivery.repository.DeliveryStatusLogRepository;
import com.example.de_tai_tt.fooddelivery.repository.MenuItemRepository;
import com.example.de_tai_tt.fooddelivery.repository.OrderRepository;
import com.example.de_tai_tt.fooddelivery.repository.RestaurantRepository;
import com.example.de_tai_tt.fooddelivery.repository.UserRepository;
import com.example.de_tai_tt.fooddelivery.repository.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.example.de_tai_tt.fooddelivery.FoodDeliveryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FoodDeliveryApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeliveryStatusLogRepository statusLogRepository;

    Restaurant restaurant;
    Restaurant otherRestaurant;
    MenuItem pho;
    MenuItem sushi;

    @BeforeEach
    void setUp() {
        statusLogRepository.deleteAll();
        orderRepository.deleteAll();
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();
        voucherRepository.deleteAll();
        deliveryAgentRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(AppUser.builder()
                .username("admin")
                .email("admin@test.local")
                .password(passwordEncoder.encode("admin123"))
                .fullName("Admin")
                .roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER))
                .enabled(true)
                .build());
        userRepository.save(AppUser.builder()
                .username("user")
                .email("user@test.local")
                .password(passwordEncoder.encode("user123"))
                .fullName("User")
                .roles(Set.of(Role.ROLE_USER))
                .enabled(true)
                .build());

        restaurant = restaurantRepository.save(Restaurant.builder()
                .name("Pho House")
                .cuisineType("Vietnamese")
                .address("1 Nguyen Hue")
                .latitude(10.7758)
                .longitude(106.7019)
                .active(true)
                .build());
        otherRestaurant = restaurantRepository.save(Restaurant.builder()
                .name("Sushi Bar")
                .cuisineType("Japanese")
                .address("22 Le Loi")
                .latitude(10.7731)
                .longitude(106.7040)
                .active(true)
                .build());
        pho = menuItemRepository.save(MenuItem.builder()
                .restaurant(restaurant)
                .name("Pho Bo")
                .description("Beef noodle soup")
                .price(BigDecimal.valueOf(65000))
                .available(true)
                .build());
        sushi = menuItemRepository.save(MenuItem.builder()
                .restaurant(otherRestaurant)
                .name("Sushi Set")
                .description("Sushi")
                .price(BigDecimal.valueOf(120000))
                .available(true)
                .build());
        voucherRepository.save(Voucher.builder()
                .code("SAVE10")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(BigDecimal.valueOf(10))
                .minOrderAmount(BigDecimal.valueOf(50000))
                .expiresAt(LocalDate.now().plusDays(7))
                .usageLimit(5)
                .usedCount(0)
                .active(true)
                .build());
        voucherRepository.save(Voucher.builder()
                .code("OLD")
                .discountType(DiscountType.FIXED_AMOUNT)
                .discountValue(BigDecimal.valueOf(10000))
                .minOrderAmount(BigDecimal.ZERO)
                .expiresAt(LocalDate.now().minusDays(1))
                .usageLimit(5)
                .usedCount(0)
                .active(true)
                .build());
        deliveryAgentRepository.save(DeliveryAgent.builder()
                .fullName("Fast Driver")
                .phone("0911111111")
                .currentLatitude(10.7760)
                .currentLongitude(106.7000)
                .status(DeliveryAgentStatus.AVAILABLE)
                .build());
    }

    @Test
    void publicRestaurantAndMenuApisDoNotRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/api/restaurants/{id}/menu", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pho Bo"));
    }

    @Test
    void registerAndLoginReturnJwtTokens() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "newuser",
                                  "email": "newuser@test.local",
                                  "password": "secret123",
                                  "fullName": "New User"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.roles[0]").value("ROLE_USER"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "newuser",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void adminApiRejectsRegularUsers() throws Exception {
        mockMvc.perform(post("/api/admin/restaurants")
                        .header("Authorization", bearer(userToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Admin Only",
                                  "cuisineType": "Fusion",
                                  "address": "Hidden",
                                  "latitude": 10.1,
                                  "longitude": 106.1,
                                  "active": true
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreateRestaurant() throws Exception {
        mockMvc.perform(post("/api/admin/restaurants")
                        .header("Authorization", bearer(adminToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Rice Corner",
                                  "cuisineType": "Vietnamese",
                                  "address": "3 Pasteur",
                                  "latitude": 10.77,
                                  "longitude": 106.70,
                                  "active": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rice Corner"));
    }

    @Test
    void voucherValidationChecksAmountLimitAndExpiry() throws Exception {
        mockMvc.perform(post("/api/vouchers/validate")
                        .header("Authorization", bearer(userToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "SAVE10",
                                  "orderAmount": 130000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.discountAmount").value(13000.00));

        mockMvc.perform(post("/api/vouchers/validate")
                        .header("Authorization", bearer(userToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "OLD",
                                  "orderAmount": 130000
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VOUCHER_INVALID"));
    }

    @Test
    void creatingOrderAppliesVoucherAssignsAgentAndWritesInitialLog() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .header("Authorization", bearer(userToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    {"menuItemId": %d, "quantity": 2}
                                  ],
                                  "voucherCode": "SAVE10",
                                  "deliveryAddress": "10 Test Street",
                                  "note": "Less onion"
                                }
                                """.formatted(pho.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.discountAmount").value(13000.00))
                .andExpect(jsonPath("$.deliveryAgentId").exists())
                .andExpect(jsonPath("$.items", hasSize(1)));

        assertThat(voucherRepository.findByCodeIgnoreCase("SAVE10").orElseThrow().getUsedCount()).isEqualTo(1);
        assertThat(statusLogRepository.findAll()).hasSize(1);
    }

    @Test
    void orderRejectsItemsFromMultipleRestaurants() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .header("Authorization", bearer(userToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    {"menuItemId": %d, "quantity": 1},
                                    {"menuItemId": %d, "quantity": 1}
                                  ],
                                  "deliveryAddress": "10 Test Street"
                                }
                                """.formatted(pho.getId(), sushi.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ORDER_RESTAURANT_MISMATCH"));
    }

    @Test
    void adminStatusUpdateMustFollowStrictWorkflowAndWritesLogs() throws Exception {
        long orderId = createOrderAndReturnId();

        mockMvc.perform(patch("/api/admin/orders/{id}/status", orderId)
                        .header("Authorization", bearer(adminToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "DELIVERING",
                                  "note": "Skipping preparation"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ORDER_STATUS_TRANSITION_INVALID"));

        mockMvc.perform(patch("/api/admin/orders/{id}/status", orderId)
                        .header("Authorization", bearer(adminToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "PREPARING",
                                  "note": "Kitchen accepted"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PREPARING"));

        assertThat(statusLogRepository.findByOrderIdOrderByCreatedAtAsc(orderId)).hasSize(2);
    }

    @Test
    void adminDashboardReturnsCoreMetrics() throws Exception {
        long orderId = createOrderAndReturnId();
        transition(orderId, "PREPARING");
        transition(orderId, "DELIVERING");
        transition(orderId, "COMPLETED");

        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", bearer(adminToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ordersByStatus.COMPLETED").value(1))
                .andExpect(jsonPath("$.topSellingItems[0].itemName").value("Pho Bo"))
                .andExpect(jsonPath("$.topSellingItems[0].quantitySold", greaterThan(0)));
    }

    private long createOrderAndReturnId() throws Exception {
        String body = mockMvc.perform(post("/api/orders")
                        .header("Authorization", bearer(userToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    {"menuItemId": %d, "quantity": 1}
                                  ],
                                  "deliveryAddress": "10 Test Street"
                                }
                                """.formatted(pho.getId())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode json = objectMapper.readTree(body);
        return json.get("id").asLong();
    }

    private void transition(long orderId, String status) throws Exception {
        mockMvc.perform(patch("/api/admin/orders/{id}/status", orderId)
                        .header("Authorization", bearer(adminToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "%s"
                                }
                                """.formatted(status)))
                .andExpect(status().isOk());
    }

    private String userToken() throws Exception {
        return tokenFor("user", "user123");
    }

    private String adminToken() throws Exception {
        return tokenFor("admin", "admin123");
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private String tokenFor(String username, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "password": "%s"
                                }
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(body).get("token").asText();
    }
}
