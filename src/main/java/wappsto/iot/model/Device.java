package wappsto.iot.model;

import wappsto.iot.model.schema.network.*;

import java.util.*;

public class Device {

    private final UUID id;
    private HashMap<String, Value> values;

    public Device(UUID id, Collection<ValueSchema> values) {
        this.id = id;

        for (ValueSchema v: values) {
            this.values.put(v.name, new Value(v.meta.id));
        }
    }
}
