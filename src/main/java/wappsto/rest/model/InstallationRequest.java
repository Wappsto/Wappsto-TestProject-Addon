package wappsto.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstallationRequest {
    @JsonProperty
    public String application;

    public InstallationRequest(String application) {
        this.application = application;
    }
}
