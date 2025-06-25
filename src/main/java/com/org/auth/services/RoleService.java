package com.org.auth.services;

import com.org.auth.model.AuthRequest;
import com.org.auth.model.RoleName;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RoleService {
    ResponseEntity<?> registerNewUser(AuthRequest request);
    ResponseEntity<?> assignRole(Long userId, RoleName roleName);
    ResponseEntity<?> revokeRoleForUser(Long userId, RoleName roleName);
    List<Map<String, Object>> getAllUser();
}
