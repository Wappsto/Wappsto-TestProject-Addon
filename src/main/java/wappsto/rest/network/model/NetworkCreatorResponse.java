package wappsto.rest.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkCreatorResponse {
    @JsonProperty
    public Network network;

    @JsonProperty
    public String ca;

    @JsonProperty
    public String certificate;

    @JsonProperty("private_key")
    public String privateKey;
}
