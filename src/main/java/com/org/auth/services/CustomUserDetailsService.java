package com.org.auth.services;

import com.org.auth.entity.RoleEntity;
import com.org.auth.entity.UserEntity;
import com.org.auth.model.RoleName;
import com.org.auth.repository.RoleRepository;
import com.org.auth.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final RoleRepository roleRepo;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Map<String, UserDetails> USERS = new HashMap<>();

    @PostConstruct
    public void initAdminIfMissing() {
        userRepository.findByUsername(adminUsername).orElseGet(() -> {
            UserEntity admin = new UserEntity();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            // Assign admin role (you may need to create/find it first)
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName(RoleName.ADMIN);
            admin.setRoles(Set.of(roleRepo.save(adminRole)));
            return userRepository.save(admin);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .collect(Collectors.toList())
        );
    }
}
