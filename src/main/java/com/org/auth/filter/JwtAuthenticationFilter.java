package com.org.auth.filter;

import com.org.auth.services.AuthService;
import com.org.auth.services.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = authService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = authService.extractAllClaims(jwt);

            // ✅ Extract roles and scopes from JWT
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            // extract 'roles' if available
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof List<?> roleList) {
                roleList.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
            }

            // extract 'scope' and convert each to authority
            String scopeString = (String) claims.get("scope");
            if (scopeString != null && !scopeString.isBlank()) {
                Arrays.stream(scopeString.split(" "))
                        .forEach(scope -> authorities.add(new SimpleGrantedAuthority(scope)));
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
