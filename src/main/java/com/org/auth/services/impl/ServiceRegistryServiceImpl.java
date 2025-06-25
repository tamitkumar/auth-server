package com.org.auth.services.impl;

import com.org.auth.entity.ScopeRequestEntity;
import com.org.auth.entity.ServiceEntity;
import com.org.auth.model.ServiceRegistrationRequest;
import com.org.auth.repository.ScopeRequestRepository;
import com.org.auth.repository.ServiceRepository;
import com.org.auth.services.ServiceRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRegistryServiceImpl implements ServiceRegistryService {
    private final ServiceRepository serviceRepo;
    private final ScopeRequestRepository scopeRepo;
    @Override
    public ResponseEntity<?> registerService(ServiceRegistrationRequest request) {
        if (serviceRepo.findByServiceName(request.getServiceName()).isPresent()) {
            return ResponseEntity.badRequest().body(request.getServiceName() + " Service already registered");
        }

        ServiceEntity service = new ServiceEntity();
        service.setServiceName(request.getServiceName());
        service.setApproved(false);
        return ResponseEntity.ok(serviceRepo.save(service));
    }

    @Override
    public ResponseEntity<?> approveService(Long serviceId) {
        ServiceEntity service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setApproved(true);
        serviceRepo.save(service);
        return ResponseEntity.ok("Service approved: " + service.getServiceName());
    }

    @Override
    public List<ServiceEntity> getAllServices() {
        return serviceRepo.findAll();
    }

    @Override
    public ResponseEntity<?> requestScope(Long serviceId, String scope) {
        ServiceEntity service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!service.isApproved())
            return ResponseEntity.badRequest().body("Service not yet approved");

        ScopeRequestEntity request = new ScopeRequestEntity();
        request.setService(service);
        request.setScope(scope);
        request.setApproved(false);
        scopeRepo.save(request);
        return ResponseEntity.ok("Scope request submitted");
    }

    @Override
    public ResponseEntity<?> approveScope(Long serviceId, String scope) {
        ScopeRequestEntity scopeEntity = scopeRepo.findByScopeAndServiceId(scope, serviceId)
                .orElseThrow(() -> new RuntimeException("Scope request not found"));
        scopeEntity.setApproved(true);
        return ResponseEntity.ok(scopeRepo.save(scopeEntity));
    }

    @Override
    public List<String> getApprovedScopes(Long serviceId) {
        return scopeRepo.findByServiceId(serviceId).stream()
                .filter(ScopeRequestEntity::isApproved)
                .map(ScopeRequestEntity::getScope)
                .toList();
    }

    @Override
    public List<ScopeRequestEntity> getAllScopes(Long serviceId) {
        return scopeRepo.findByServiceId(serviceId);
    }
}
