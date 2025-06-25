//package com.org.auth.config;
//
//import com.org.auth.entity.OAuthClient;
//import com.org.auth.repository.OAuthClientRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
//import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.util.stream.Collectors;
//
//@Component
//public class JpaRegisteredClientRepository implements RegisteredClientRepository {
//    private final OAuthClientRepository clientRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public JpaRegisteredClientRepository(OAuthClientRepository clientRepository, PasswordEncoder passwordEncoder) {
//        this.clientRepository = clientRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void save(RegisteredClient registeredClient) {
//        OAuthClient client = new OAuthClient();
//        client.setClientId(registeredClient.getClientId());
//        client.setClientSecret(passwordEncoder.encode(registeredClient.getClientSecret()));
//        client.setAuthenticationMethods(registeredClient.getClientAuthenticationMethods()
//                .stream()
//                .map(ClientAuthenticationMethod::getValue)
//                .collect(Collectors.toSet()));
//
//        client.setGrantTypes(registeredClient.getAuthorizationGrantTypes()
//                .stream()
//                .map(AuthorizationGrantType::getValue)
//                .collect(Collectors.toSet()));
//
//        client.setScopes(registeredClient.getScopes());
//        client.setAccessTokenValidity(Duration.ofMinutes(30));
//        client.setRefreshTokenValidity(Duration.ofHours(2));
//        clientRepository.save(client);
//    }
//
//    @Override
//    public RegisteredClient findById(String id) {
//        return clientRepository.findById(Long.valueOf(id))
//                .map(this::convertToRegisteredClient)
//                .orElse(null);
//    }
//
//    @Override
//    public RegisteredClient findByClientId(String clientId) {
//        return clientRepository.findByClientId(clientId)
//                .map(this::convertToRegisteredClient)
//                .orElse(null);
//    }
//
//    private RegisteredClient convertToRegisteredClient(OAuthClient client) {
//        return RegisteredClient.withId(String.valueOf(client.getId()))
//                .clientId(client.getClientId())
//                .clientSecret(client.getClientSecret())
//                .clientAuthenticationMethods(authMethods ->
//                        authMethods.addAll(
//                                client.getAuthenticationMethods().stream()
//                                        .map(ClientAuthenticationMethod::new)
//                                        .collect(Collectors.toSet())
//                        ))
//                .authorizationGrantTypes(grantTypes ->
//                        grantTypes.addAll(
//                                client.getGrantTypes().stream()
//                                        .map(AuthorizationGrantType::new)
//                                        .collect(Collectors.toSet())
//                        ))
//                .scopes(scopes -> scopes.addAll(client.getScopes()))
//                .tokenSettings(TokenSettings.builder()
//                        .accessTokenTimeToLive(client.getAccessTokenValidity())
//                        .refreshTokenTimeToLive(client.getRefreshTokenValidity())
//                        .build())
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
//                .build();
//    }
//}
