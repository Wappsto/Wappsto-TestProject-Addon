package wappsto.rest.model;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WappsResponse {
    @JsonProperty
    public List<String> id;
}
