package com.org.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRegistrationRequest {

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("description")
    private String description;
}
