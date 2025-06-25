package com.org.auth.config;

import com.org.auth.entity.UserEntity;
import com.org.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthServerConfig {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final PasswordEncoder passwordEncoder;

    public AuthServerConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            if (repo.findByUsername(adminUsername).isEmpty()) {
                repo.save(
                        UserEntity.builder()
                                .username(adminUsername)
                                .password(passwordEncoder.encode(adminPassword))
                                .build()
                );
                System.out.println("Admin user created.");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
//    @Bean
//    public ResponseEntity<String> registerClient(JpaRegisteredClientRepository clientRepository,
//                                                 PasswordEncoder passwordEncoder) {
//        if (clientRepository.findByClientId(adminUsername) != null) {
//            return ResponseEntity.badRequest().body("Client ID already exists");
//        }
//
//        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId(adminUsername)
//                .clientSecret(passwordEncoder.encode(adminPassword))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .tokenSettings(TokenSettings.builder()
//                        .accessTokenTimeToLive(Duration.ofMinutes(30))
//                        .refreshTokenTimeToLive(Duration.ofHours(2))
//                        .build())
//                .scopes(scopes -> {
//                    scopes.add("read");
//                    scopes.add("create");
//                    scopes.add("update");
//                    scopes.add("delete");
//                })
//                .clientSettings(ClientSettings.builder()
//                        .requireAuthorizationConsent(false)
//                        .build())
//                .build();
//
//        clientRepository.save(client);
//        return ResponseEntity.ok("Client registered successfully");
//    }
}
