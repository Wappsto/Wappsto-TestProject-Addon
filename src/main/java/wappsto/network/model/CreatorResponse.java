package wappsto.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatorResponse {
    @JsonProperty
    public NetworkMeta network;

    @JsonProperty
    public String ca;

    @JsonProperty
    public String certificate;

    @JsonProperty("private_key")
    public String privateKey;
}
