CREATE TABLE auth.oauth_clients (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   client_id VARCHAR(255) NOT NULL UNIQUE,
   client_secret VARCHAR(255) NOT NULL,
   access_token_validity BIGINT,
   refresh_token_validity BIGINT
);

CREATE TABLE auth.client_auth_methods (
   client_id BIGINT NOT NULL,
   auth_method VARCHAR(255),
   FOREIGN KEY (client_id) REFERENCES auth.oauth_clients(id) ON DELETE CASCADE
);

CREATE TABLE auth.client_grant_types (
   client_id BIGINT NOT NULL,
   grant_type VARCHAR(255),
   FOREIGN KEY (client_id) REFERENCES auth.oauth_clients(id) ON DELETE CASCADE
);

CREATE TABLE auth.client_scopes (
    client_id BIGINT NOT NULL,
    scope VARCHAR(255),
    FOREIGN KEY (client_id) REFERENCES auth.oauth_clients(id) ON DELETE CASCADE
);

CREATE TABLE auth.registered_services (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   service_name VARCHAR(255) NOT NULL UNIQUE,
   approved BOOLEAN DEFAULT FALSE
);

CREATE TABLE auth.scope_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    scope VARCHAR(255),
    approved BOOLEAN DEFAULT FALSE,
    service_id BIGINT,
    CONSTRAINT fk_scope_requests_service
    FOREIGN KEY (service_id)
    REFERENCES auth.registered_services(id)
    ON DELETE CASCADE
);

select * from auth.oauth_clients;