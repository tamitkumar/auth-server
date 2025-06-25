package com.org.auth.repository;

import com.org.auth.entity.MicroserviceRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MicroserviceRegisterRepository extends JpaRepository<MicroserviceRegisterEntity, Long> {
    Optional<MicroserviceRegisterEntity> findByClientId(String clientId);
}
