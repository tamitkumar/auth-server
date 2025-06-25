package com.org.auth.controller;

import com.org.auth.entity.ServiceEntity;
import com.org.auth.entity.UserEntity;
import com.org.auth.model.AuthRequest;
import com.org.auth.model.AuthResponse;
import com.org.auth.model.MicroserviceRegister;
import com.org.auth.model.RequestStatus;
import com.org.auth.repository.OAuthClientRepository;
import com.org.auth.repository.ServiceRepository;
import com.org.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;
    private final ServiceRepository serviceRepository;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody AuthRequest request) {
        UserEntity user = authService.validateUser(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String serviceName = request.getServiceName();
        ServiceEntity service = serviceRepository.findByServiceName(serviceName)
                .orElse(null);

        if (service == null) {
            return ResponseEntity.badRequest().body("Requested service not registered: " + serviceName);
        }
        if (!service.isApproved()) {
            return ResponseEntity.status(403).body("Service not approved yet: " + serviceName);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = authService.generateToken(userDetails, serviceName);
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
