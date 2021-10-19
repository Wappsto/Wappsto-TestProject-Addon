package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

import java.util.*;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValueSchema {
    @JsonProperty public String name;
    @JsonProperty public String permission;
    @JsonProperty public List<StateSchema> state;
    @JsonProperty public NumberSchema number;
    @JsonProperty public Meta meta;
    @JsonProperty public String type;

    public ValueSchema(
        String name,
        String type,
        String permission,
        NumberSchema numbers
    )
        throws Exception
    {
        this(name, type, ValuePermission.from(permission), numbers);
    }

    public ValueSchema(
        String name,
        String type,
        ValuePermission permission,
        NumberSchema numbers
    ) {
        this.name = name;
        this.type = type;
        this.permission = permission.toString();
        state = new LinkedList<>();
        if (this.permission.contains("r")) state.add(new StateSchema("Report"));
        if (this.permission.contains("w")) state.add(new StateSchema("Control"));
        this.number = numbers;
        meta = new Meta("Value");
    }

    public ValueSchema() {}
}
