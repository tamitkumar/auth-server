package com.org.auth.controller;

import com.org.auth.services.ServiceRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scope/registry")
@RequiredArgsConstructor
public class ScopeController {

    private final ServiceRegistryService serviceService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/approve")
    public ResponseEntity<?> approveScope(@RequestParam Long serviceId, @RequestParam String scope) {
        return serviceService.approveScope(serviceId, scope);
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestScope(@RequestParam Long serviceId, @RequestParam String scope) {
        return serviceService.requestScope(serviceId, scope);
    }

    @GetMapping("/list/{serviceId}")
    public ResponseEntity<?> listScopes(@PathVariable Long serviceId) {
        return ResponseEntity.ok(serviceService.getAllScopes(serviceId));
    }
}
