package com.org.auth.repository;

import com.org.auth.entity.ScopeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScopeRequestRepository extends JpaRepository<ScopeRequestEntity, Long> {
    List<ScopeRequestEntity> findByServiceId(Long serviceId);
    Optional<ScopeRequestEntity> findByScopeAndServiceId(String scope, Long serviceId);
}
