package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

import java.util.*;
public class ValueSchema {
    @JsonProperty public String name;
    @JsonProperty public String permission;
    @JsonProperty public List<StateSchema> state;
    @JsonProperty public NumberSchema numbers;
    @JsonProperty public Meta meta;

    public ValueSchema(String name, String permission, NumberSchema numbers)
        throws Exception
    {
        this(name, ValuePermission.from(permission), numbers);
    }

    public ValueSchema(
        String name,
        ValuePermission permission,
        NumberSchema numbers
    ) {
        this.name = name;
        this.permission = permission.toString();
        state = new LinkedList<>();
        if (this.permission.contains("r")) state.add(new StateSchema("Report"));
        if (this.permission.contains("w")) state.add(new StateSchema("Control"));
        this.numbers = numbers;
        meta = new Meta("Value");
    }

    public ValueSchema() {}

    public static class Builder {
        private final String name;
        private final ValuePermission permission;
        private final DeviceSchema.Builder device;
        private NumberSchema numberSchema;

        public Builder(
            String name,
            ValuePermission permission,
            DeviceSchema.Builder device
        ) {
            this.name = name;
            this.permission = permission;
            this.device = device;
        }


        public Builder withNumberSchema(
            int min,
            int max,
            int stepSize,
            String type
        ) {
            return withNumberSchema(new NumberSchema(min, max, stepSize, type));
        }

        public DeviceSchema.Builder addToDevice() {
            if(numberSchema == null) {
                throw new RuntimeException("Number schema can't be null");
            }

            device.add(new ValueSchema(name, permission, numberSchema));
            return device;
        }

        public Builder withNumberSchema(NumberSchema numbers) {
            this.numberSchema = numbers;
            return this;
        }
    }
}
