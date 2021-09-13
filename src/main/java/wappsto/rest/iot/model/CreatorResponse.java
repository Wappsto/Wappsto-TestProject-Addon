package wappsto.rest.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatorResponse {
    @JsonProperty
    public Network network;

    @JsonProperty
    public String ca;

    @JsonProperty
    public String certificate;

    @JsonProperty("private_key")
    public String privateKey;
}
