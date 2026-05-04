package com.example.de_tai_tt.fooddelivery.service;

import com.example.de_tai_tt.fooddelivery.dto.AuthRequest;
import com.example.de_tai_tt.fooddelivery.dto.AuthResponse;
import com.example.de_tai_tt.fooddelivery.dto.RegisterRequest;
import com.example.de_tai_tt.fooddelivery.entity.AppUser;
import com.example.de_tai_tt.fooddelivery.entity.Role;
import com.example.de_tai_tt.fooddelivery.exception.BusinessException;
import com.example.de_tai_tt.fooddelivery.exception.ErrorCode;
import com.example.de_tai_tt.fooddelivery.mapper.UserMapper;
import com.example.de_tai_tt.fooddelivery.repository.UserRepository;
import com.example.de_tai_tt.fooddelivery.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "Email already exists");
        }

        AppUser user = AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .phone(request.phone())
                .address(request.address())
                .roles(Set.of(Role.ROLE_USER))
                .enabled(true)
                .build();
        AppUser saved = userRepository.save(user);
        UserDetails details = userDetailsService.loadUserByUsername(saved.getUsername());
        return new AuthResponse(jwtService.generateToken(details), "Bearer", userMapper.toDto(saved));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_CREDENTIALS, "Invalid username or password"));
        UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
        return new AuthResponse(jwtService.generateToken(details), "Bearer", userMapper.toDto(user));
    }
}
