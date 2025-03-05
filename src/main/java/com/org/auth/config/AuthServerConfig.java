package com.org.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthServerConfig {


    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        String encodedSecret1 = passwordEncoder.encode("s1-client-secret");
        String encodedSecret2 = passwordEncoder.encode("s2-client-secret");
        System.out.println("Encoded Secret for s1-client-id: " + encodedSecret1);
        RegisteredClient s1Client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("s1-client-id")
                .clientSecret(encodedSecret1) // Proper encoding
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofHours(2))
                        .build())
                .scopes(scopes -> {
                    scopes.add("read");
                    scopes.add("create");
                    scopes.add("update");
                    scopes.add("delete");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();
        System.out.println("Encoded Secret for s2-client-id: " + encodedSecret2);
        RegisteredClient s2Client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("s2-client-id")
                .clientSecret(encodedSecret2) // Proper encoding
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofHours(2))
                        .build())
                .scopes(scopes -> {
                    scopes.add("read");
                    scopes.add("create");
                    scopes.add("update");
                    scopes.add("delete");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();
        return new InMemoryRegisteredClientRepository(s1Client, s2Client);
    }
}
