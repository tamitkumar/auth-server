package com.org.auth.controller;

import com.org.auth.model.ServiceRegistrationRequest;
import com.org.auth.services.ServiceRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registry/services")
public class ServiceRegistryController {
    private final ServiceRegistryService registryService;
    @PostMapping("/register")
    public ResponseEntity<?> registerService(@RequestBody ServiceRegistrationRequest request) {
        return registryService.registerService(request);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/approve/{serviceId}")
    public ResponseEntity<?> approveService(@PathVariable Long serviceId) {
        return registryService.approveService(serviceId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<?> listServices() {
        return ResponseEntity.ok(registryService.getAllServices());
    }

}
