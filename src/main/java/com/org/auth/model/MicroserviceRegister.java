package com.org.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MicroserviceRegister {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("microservice_name")
    private String microserviceName;

    @JsonProperty("requested_scope")
    private String requestedScope;

    @JsonProperty("status")
    private RequestStatus status;
}
