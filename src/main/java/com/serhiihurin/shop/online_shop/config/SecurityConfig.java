package com.serhiihurin.shop.online_shop.config;

import com.serhiihurin.shop.online_shop.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.serhiihurin.shop.online_shop.enums.Permission.*;
import static com.serhiihurin.shop.online_shop.enums.Role.ADMIN;
import static com.serhiihurin.shop.online_shop.enums.Role.SHOP_OWNER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/shop-owner/**").hasAnyRole(ADMIN.name(), SHOP_OWNER.name())
                        .requestMatchers(GET, "/api/v1/shop-owner/**").hasAnyAuthority(
                                ADMIN_READ.name(),
                                SHOP_OWNER_READ.name()
                                )
                        .requestMatchers(POST, "/api/v1/shop-owner/**").hasAnyAuthority(
                                ADMIN_CREATE.name(),
                                SHOP_OWNER_CREATE.name()
                                )
                        .requestMatchers(PUT, "/api/v1/shop-owner/**").hasAnyAuthority(
                                ADMIN_UPDATE.name(),
                                SHOP_OWNER_UPDATE.name()
                                )
                        .requestMatchers(DELETE, "/api/v1/shop-owner/**").hasAnyAuthority(
                                ADMIN_DELETE.name(),
                                SHOP_OWNER_DELETE.name()
                                )
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }
}
