package com.org.auth.repository;

import com.org.auth.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    Optional<ServiceEntity> findByServiceName(String serviceName);
}
