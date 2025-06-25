package com.org.auth.services;

import com.org.auth.entity.UserEntity;
import com.org.auth.model.MicroserviceRegister;
import com.org.auth.model.RequestStatus;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface AuthService {
    UserEntity validateUser(String username, String password);
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> resolver);
    boolean isTokenValid(String token, UserDetails userDetails);
    String generateToken(UserDetails userDetails, String serviceName);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    boolean isTokenValid(String token, String username);
    String requestScopeAccess(MicroserviceRegister grantScope);
    String approveScopeAccess(Long requestId, RequestStatus status);
    List<MicroserviceRegister> getAllStatus();
    Claims extractAllClaims(String token);
}
