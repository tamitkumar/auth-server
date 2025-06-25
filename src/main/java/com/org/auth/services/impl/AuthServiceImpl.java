package com.org.auth.services.impl;

import com.org.auth.entity.MicroserviceRegisterEntity;
import com.org.auth.entity.ServiceEntity;
import com.org.auth.entity.UserEntity;
import com.org.auth.model.MicroserviceRegister;
import com.org.auth.model.RequestStatus;
import com.org.auth.repository.MicroserviceRegisterRepository;
import com.org.auth.repository.ScopeRequestRepository;
import com.org.auth.repository.ServiceRepository;
import com.org.auth.repository.UserRepository;
import com.org.auth.services.AuthService;
import com.org.auth.services.ServiceRegistryService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String SECRET_KEY = "MDEyMzQ1Njc4OUFCQ0RFRkdISUpLTE1OT1BRUlNUVVZXWFla";
    private final MicroserviceRegisterRepository microserviceRegisterRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceRepository serviceRepo;
    private final ServiceRegistryService registryService;

    @Override
    public UserEntity validateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build().parseSignedClaims(token).getPayload();
    }

    @Override
    public String generateToken(UserDetails userDetails, String serviceName) {
        ServiceEntity service = serviceRepo.findByServiceName(serviceName)
                .orElseThrow(() -> new RuntimeException("Service not registered"));
        if (!service.isApproved())
            throw new RuntimeException("Service not approved");

        List<String> approvedScopes = registryService.getApprovedScopes(service.getId());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("scope", String.join(" ", approvedScopes));
        return generateToken(extraClaims, userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .and() // closes the claims() block
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // ✅ Accepts byte[]
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && isTokenExpired(token);
    }

    @Override
    public String requestScopeAccess(MicroserviceRegister grantScope) {
        AtomicReference<String> access = new AtomicReference<>();
        microserviceRegisterRepository.findByClientId(grantScope.getClientId())
                .ifPresentOrElse(existing -> {
                    access.set("Requested Access Status: " + existing.getStatus());
                }, () -> {
                    MicroserviceRegisterEntity microserviceRegisterEntity = new MicroserviceRegisterEntity();
                    grantScope.setStatus(RequestStatus.PENDING);
                    BeanUtils.copyProperties(grantScope, microserviceRegisterEntity);
                    microserviceRegisterEntity = microserviceRegisterRepository.save(microserviceRegisterEntity);
                    access.set("Requested Access" + microserviceRegisterEntity.getStatus());
                });
        return access.get();
    }

    @Override
    public String approveScopeAccess(Long requestId, RequestStatus status) {
        AtomicReference<String> approveScope = new AtomicReference<>();
        microserviceRegisterRepository.findById(requestId).ifPresentOrElse(existing -> {
            existing.setStatus(status);
            MicroserviceRegisterEntity saved = microserviceRegisterRepository.save(existing);
            approveScope.set(saved.getStatus().toString());
        }, () -> {
            approveScope.set("NOT FOUND");
        });
        return approveScope.get();
    }

    @Override
    public List<MicroserviceRegister> getAllStatus() {
        List<MicroserviceRegister> allScopes = new ArrayList<>();
        List<MicroserviceRegisterEntity> microserviceRegisterEntity =  microserviceRegisterRepository.findAll();
        microserviceRegisterEntity.forEach(microserviceRegister -> {
            MicroserviceRegister register = new MicroserviceRegister();
            BeanUtils.copyProperties(microserviceRegister, register);
            allScopes.add(register);
        });
        return allScopes;
    }
}
