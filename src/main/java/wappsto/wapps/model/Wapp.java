package wappsto.wapps.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wapp {
    @JsonProperty public String name;
    @JsonProperty public String application;

}
