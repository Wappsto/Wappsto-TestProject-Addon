package wappsto.rest.wapps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wapp {
    @JsonProperty public String name;
    @JsonProperty public String application;

}
