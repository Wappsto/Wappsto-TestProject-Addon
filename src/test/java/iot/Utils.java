package iot;

import wappsto.iot.rpc.model.schema.network.*;
import wappsto.iot.rpc.model.schema.network.Number;
import wappsto.rest.network.model.*;

import java.util.*;

public class Utils {
    public static Params defaultNetwork(CreatorResponse creatorResponse) {
        Value value = new Value(
            "On/off",
            "rw",
            new Number(
                0,
                1,
                1,
                "Boolean"
            )
        );
        LinkedList<Value> values = new LinkedList<>();
        values.add(value);

        Device device = new Device(
            "Switch",
            values
        );

        LinkedList<Device> devices = new LinkedList<>();
        devices.add(device);
        Network data = new Network(
            "On/off switch",
            creatorResponse.network.id,
            devices
        );

        return new Params(
            "/network",
            data
        );
    }
}
