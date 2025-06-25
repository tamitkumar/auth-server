package com.org.auth.config;

import com.org.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    @Value("${admin.username}")
//    private String adminUsername;
//
//    @Value("${admin.password}")
//    private String adminPassword;
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy JwtAuthenticationFilter oncePerRequestFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/token").permitAll()
                        .requestMatchers("/api/roles/promote/**").hasRole("ADMIN")
                        .requestMatchers("/api/roles/demote/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/roles/user").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/roles/users-with-roles").hasAnyRole("ADMIN", "MANAGER", "CLIENT", "SERVICE")

                        // ✅ Add these for service registry
                        .requestMatchers("/api/registry/services/register").permitAll() // allow anyone to register a service
                        .requestMatchers("/api/registry/services/approve/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/registry/services/all").hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers("/api/scope/registry/request").permitAll() // allow anyone to register a service
                        .requestMatchers("/api/scope/registry/list/**").permitAll()
                        .requestMatchers("/api/scope/registry/approve").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(oncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
