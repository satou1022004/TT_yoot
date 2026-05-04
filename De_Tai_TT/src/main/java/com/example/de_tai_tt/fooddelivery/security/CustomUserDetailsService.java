package com.example.de_tai_tt.fooddelivery.security;

import com.example.de_tai_tt.fooddelivery.entity.AppUser;
import com.example.de_tai_tt.fooddelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .disabled(!appUser.isEnabled())
                .authorities(appUser.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList())
                .build();
    }
}
