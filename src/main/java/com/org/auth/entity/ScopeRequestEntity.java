package com.org.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scope_requests", schema = "auth")
public class ScopeRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String scope; // e.g., example-api-v1::read
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;
}
