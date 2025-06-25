package com.org.auth.services;

import com.org.auth.entity.ScopeRequestEntity;
import com.org.auth.entity.ServiceEntity;
import com.org.auth.model.ServiceRegistrationRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServiceRegistryService {
    ResponseEntity<?> registerService(ServiceRegistrationRequest request);
    ResponseEntity<?> approveService(Long serviceId);
    List<ServiceEntity> getAllServices();
    ResponseEntity<?> approveScope(Long serviceId, String scope);
    List<String> getApprovedScopes(Long serviceId);
    List<ScopeRequestEntity> getAllScopes(Long serviceId);
    ResponseEntity<?> requestScope(Long serviceId, String scope);
}
