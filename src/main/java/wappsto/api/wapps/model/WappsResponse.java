package wappsto.api.wapps.model;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WappsResponse {
    @JsonProperty
    public List<String> id;
}
