package wappsto.rest.wapps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketResponse {
    @JsonProperty("version")
    public List<Wapp> wapps;
}
