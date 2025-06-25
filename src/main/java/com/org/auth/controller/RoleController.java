package com.org.auth.controller;

import com.org.auth.model.AuthRequest;
import com.org.auth.model.RoleName;
import com.org.auth.repository.UserRepository;
import com.org.auth.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserRepository userRepo;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody AuthRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        return roleService.registerNewUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/promote/{userId}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long userId, @RequestParam RoleName roleName) {
        return roleService.assignRole(userId, roleName);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/demote/{userId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @RequestParam RoleName roleName) {
        return roleService.revokeRoleForUser(userId, roleName);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('CLIENT') || hasRole('SERVICE')")
    @GetMapping("/users-with-roles")
    public ResponseEntity<List<Map<String, Object>>> listUsersWithRoles() {
        return ResponseEntity.ok(roleService.getAllUser());
    }
}
