package com.org.auth.services.impl;

import com.org.auth.entity.RoleEntity;
import com.org.auth.entity.UserEntity;
import com.org.auth.model.AuthRequest;
import com.org.auth.model.RoleName;
import com.org.auth.repository.RoleRepository;
import com.org.auth.repository.UserRepository;
import com.org.auth.services.RoleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> registerNewUser(AuthRequest request) {
        log.info("Registering new user");
        AtomicReference<ResponseEntity<?>> response = new AtomicReference<>();
        roleRepo.findByName(request.getRoles())
                .ifPresentOrElse(roles -> {
                    log.info("Role found: {}", roles);
                    UserEntity newUser = new UserEntity();
                    newUser.setUsername(request.getUsername());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    buildRole(response, roles, newUser);
                }, () -> {
                    log.info("User not found");
                    UserEntity newUser = new UserEntity();
                    newUser.setUsername(request.getUsername());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    RoleEntity newRole = new RoleEntity();
                    newRole.setName(request.getRoles());
                    RoleEntity saveRole = roleRepo.save(newRole);
                    buildRole(response, saveRole, newUser);
                });
        return response.get();
    }

    @Override
    public ResponseEntity<?> assignRole(Long userId, RoleName roleName) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found to assign role"));
        RoleEntity role = roleRepo.findByName(roleName).orElseGet(() -> {
            // Save role only if not exists
            RoleEntity newRole = new RoleEntity();
            newRole.setName(roleName);
            return roleRepo.save(newRole);
        });

        // Add role if not already assigned
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
        }

        UserEntity savedUser = userRepo.save(user);

        Set<String> assignedRoles = savedUser.getRoles()
                .stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return ResponseEntity.accepted().body("User: " + savedUser.getUsername() + " Assigned Roles: " + assignedRoles);
    }

    @Override
    public ResponseEntity<?> revokeRoleForUser(Long userId, RoleName roleName) {
        log.info("Revoking role for user: {}", userId);
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        RoleEntity role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        if (user.getRoles().remove(role)) {
            userRepo.save(user);
            return ResponseEntity.ok("Role " + roleName + " removed from user " + user.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("User doesn't have role: " + roleName);
        }
    }

    @Override
    public List<Map<String, Object>> getAllUser() {
        List<UserEntity> users = userRepo.findAll();
        List<Map<String, Object>> response = users.stream().map(user -> {
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("roles", user.getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toSet()));
            return data;
        }).collect(Collectors.toList());

        return response;
    }

    private void buildRole(AtomicReference<ResponseEntity<?>> response, @NonNull RoleEntity roles, UserEntity newUser) {
        newUser.setRoles(Set.of(roles));
        UserEntity savedUser = userRepo.save(newUser);
        Set<String> assignedRoles = savedUser.getRoles()
                .stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());
        response.set(ResponseEntity.accepted().body("User: " + savedUser.getUsername() + " Assigned Roles: " + assignedRoles));
    }
}
