CREATE TABLE auth.client_microservice
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    client_id         VARCHAR(255) NULL,
    microservice_name VARCHAR(255) NULL,
    requested_scope   VARCHAR(255) NULL,
    status            VARCHAR(255) NULL,
    CONSTRAINT pk_client_microservice PRIMARY KEY (id)
);

CREATE TABLE auth.roles
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE auth.user_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE auth.users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE auth.roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE auth.users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE auth.user_roles
    ADD CONSTRAINT fk_userol_on_role_entity FOREIGN KEY (role_id) REFERENCES auth.roles (id);

ALTER TABLE auth.user_roles
    ADD CONSTRAINT fk_userol_on_user_entity FOREIGN KEY (user_id) REFERENCES auth.users (id);

CREATE TABLE auth.oauth2_registered_client (
                                          id varchar(100) NOT NULL,
                                          client_id varchar(100) NOT NULL,
                                          client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                          client_secret varchar(200),
                                          client_secret_expires_at timestamp NULL,
                                          client_name varchar(200) NOT NULL,
                                          client_authentication_methods varchar(1000) NOT NULL,
                                          authorization_grant_types varchar(1000) NOT NULL,
                                          redirect_uris varchar(1000),
                                          post_logout_redirect_uris varchar(1000),
                                          scopes varchar(1000) NOT NULL,
                                          client_settings varchar(2000) NOT NULL,
                                          token_settings varchar(2000) NOT NULL,
                                          PRIMARY KEY (id)
);