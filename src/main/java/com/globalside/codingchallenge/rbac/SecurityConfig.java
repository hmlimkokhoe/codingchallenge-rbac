package com.globalside.codingchallenge.rbac;

import com.globalside.codingchallenge.rbac.model.UserRole;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/products", "/products/**")
                            .hasAnyRole(UserRole.USER, UserRole.ADMIN)
                        .requestMatchers("/products", "/products/**")
                            .hasRole(UserRole.ADMIN)
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exc -> exc
                        .accessDeniedHandler(((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write("{\"message\": \"You may not access this resource due to insufficient rights.\"");
                        }))
                );
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        // for demo purposes only
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails user = users
                .username("user")
                .password("user123")
                .roles(UserRole.USER)
                .build();
        UserDetails admin = users
                .username("admin")
                .password("admin123")
                .roles(UserRole.USER, UserRole.ADMIN)
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
