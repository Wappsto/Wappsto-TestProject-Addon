package wappsto.api.session.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMeta {
    @JsonProperty public String id;
}
