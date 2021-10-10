package wappsto.api.wapps.model;

import com.fasterxml.jackson.annotation.*;

public class InstallationRequest {
    @JsonProperty
    public String application;

    public InstallationRequest(String application) {
        this.application = application;
    }
}
