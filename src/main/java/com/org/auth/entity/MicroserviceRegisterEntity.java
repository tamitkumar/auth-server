package com.org.auth.entity;

import com.org.auth.model.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "client_microservice", schema = "auth")
@Getter
@Setter
public class MicroserviceRegisterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "microservice_name")
    private String microserviceName;
    @Column(name = "requested_scope")
    private String requestedScope;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;
}
