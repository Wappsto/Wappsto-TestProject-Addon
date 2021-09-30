package wappsto.wapps.model;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketResponse {
    @JsonProperty("version")
    public List<Wapp> wapps;
}
