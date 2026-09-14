package com.julia.nextpoint.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/trips").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/trips/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/trips/**")
                        .hasAnyRole("ADMIN", "TRAVELER")
                        .requestMatchers("/api/bookings/**").hasRole("TRAVELER")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null) {
                return java.util.List.of();
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            if (roles == null) {
                return java.util.List.of();
            }
            return roles.stream()
                    .map(role -> (GrantedAuthority)
                            new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });
        return converter;
    }
}