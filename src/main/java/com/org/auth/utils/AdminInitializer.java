//package com.org.auth.utils;
//
//import com.org.auth.entity.RoleEntity;
//import com.org.auth.entity.UserEntity;
//import com.org.auth.repository.RoleRepository;
//import com.org.auth.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class AdminInitializer implements CommandLineRunner {
//    private final UserRepository userRepo;
//    private final RoleRepository roleRepo;
//    private final PasswordEncoder passwordEncoder;
//
//    @Value("${admin.username}")
//    private String adminUsername;
//
//    @Value("${admin.password}")
//    private String adminPassword;
//
//    @Override
//    public void run(String... args) {
//        if (adminUsername == null || adminPassword == null) {
//            System.out.println("❌ Admin credentials not provided. Skipping admin creation.");
//            return;
//        }
//
//        Optional<UserEntity> existingAdmin = userRepo.findByUsername(adminUsername);
//        if (existingAdmin.isPresent()) {
//            System.out.println("✅ Admin user already exists. Skipping creation.");
//            return;
//        }
//
//        RoleEntity adminRole = roleRepo.findByName("ADMIN").orElseGet(() -> {
//            RoleEntity newRole = new RoleEntity();
//            newRole.setName("ADMIN");
//            return roleRepo.save(newRole);
//        });
//
//        UserEntity admin = new UserEntity();
//        admin.setUsername(adminUsername);
//        admin.setPassword(passwordEncoder.encode(adminPassword));
//        admin.setRoles(Set.of(adminRole));
//        userRepo.save(admin);
//
//        System.out.println("✅ Admin user created successfully with username: " + adminUsername);
//    }
//
//
//}
