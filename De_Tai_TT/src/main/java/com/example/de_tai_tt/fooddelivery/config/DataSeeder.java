package com.example.de_tai_tt.fooddelivery.config;

import com.example.de_tai_tt.fooddelivery.entity.AppUser;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgent;
import com.example.de_tai_tt.fooddelivery.entity.DeliveryAgentStatus;
import com.example.de_tai_tt.fooddelivery.entity.DiscountType;
import com.example.de_tai_tt.fooddelivery.entity.MenuItem;
import com.example.de_tai_tt.fooddelivery.entity.Restaurant;
import com.example.de_tai_tt.fooddelivery.entity.Role;
import com.example.de_tai_tt.fooddelivery.entity.Voucher;
import com.example.de_tai_tt.fooddelivery.repository.DeliveryAgentRepository;
import com.example.de_tai_tt.fooddelivery.repository.MenuItemRepository;
import com.example.de_tai_tt.fooddelivery.repository.RestaurantRepository;
import com.example.de_tai_tt.fooddelivery.repository.UserRepository;
import com.example.de_tai_tt.fooddelivery.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final VoucherRepository voucherRepository;
    private final DeliveryAgentRepository deliveryAgentRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDemoData() {
        return args -> seed();
    }

    @Transactional
    void seed() {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .email("admin@food.local")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Admin")
                    .roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER))
                    .enabled(true)
                    .build());
        }
        if (!userRepository.existsByUsername("user")) {
            userRepository.save(AppUser.builder()
                    .username("user")
                    .email("user@food.local")
                    .password(passwordEncoder.encode("user123"))
                    .fullName("Demo User")
                    .phone("0900000000")
                    .address("District 1, Ho Chi Minh City")
                    .roles(Set.of(Role.ROLE_USER))
                    .enabled(true)
                    .build());
        }

        if (restaurantRepository.count() == 0) {
            Restaurant phoHouse = restaurantRepository.save(Restaurant.builder()
                    .name("Pho House")
                    .cuisineType("Vietnamese")
                    .address("1 Nguyen Hue, District 1")
                    .latitude(10.7758)
                    .longitude(106.7019)
                    .active(true)
                    .build());
            Restaurant sushiBar = restaurantRepository.save(Restaurant.builder()
                    .name("Sushi Bar")
                    .cuisineType("Japanese")
                    .address("22 Le Loi, District 1")
                    .latitude(10.7731)
                    .longitude(106.7040)
                    .active(true)
                    .build());

            menuItemRepository.save(MenuItem.builder()
                    .restaurant(phoHouse)
                    .name("Pho Bo")
                    .description("Beef noodle soup")
                    .price(BigDecimal.valueOf(65000))
                    .available(true)
                    .build());
            menuItemRepository.save(MenuItem.builder()
                    .restaurant(phoHouse)
                    .name("Banh Mi")
                    .description("Vietnamese baguette")
                    .price(BigDecimal.valueOf(35000))
                    .available(true)
                    .build());
            menuItemRepository.save(MenuItem.builder()
                    .restaurant(sushiBar)
                    .name("Salmon Sushi Set")
                    .description("Fresh salmon sushi")
                    .price(BigDecimal.valueOf(120000))
                    .available(true)
                    .build());
        }

        if (!voucherRepository.existsByCodeIgnoreCase("WELCOME10")) {
            voucherRepository.save(Voucher.builder()
                    .code("WELCOME10")
                    .discountType(DiscountType.PERCENTAGE)
                    .discountValue(BigDecimal.valueOf(10))
                    .minOrderAmount(BigDecimal.valueOf(50000))
                    .expiresAt(LocalDate.now().plusMonths(6))
                    .usageLimit(100)
                    .usedCount(0)
                    .active(true)
                    .build());
        }

        if (deliveryAgentRepository.count() == 0) {
            deliveryAgentRepository.save(DeliveryAgent.builder()
                    .fullName("Nguyen Van Ship")
                    .phone("0911111111")
                    .currentLatitude(10.7760)
                    .currentLongitude(106.7000)
                    .status(DeliveryAgentStatus.AVAILABLE)
                    .build());
            deliveryAgentRepository.save(DeliveryAgent.builder()
                    .fullName("Tran Thi Fast")
                    .phone("0922222222")
                    .currentLatitude(10.7800)
                    .currentLongitude(106.7050)
                    .status(DeliveryAgentStatus.AVAILABLE)
                    .build());
        }
    }
}
