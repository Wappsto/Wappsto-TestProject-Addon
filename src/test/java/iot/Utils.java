package iot;

import wappsto.iot.model.schema.network.*;
import wappsto.iot.model.schema.network.Number;
import wappsto.rest.network.model.*;

import java.util.*;

public class Utils {
    public static NetworkSchema defaultNetwork(CreatorResponse creatorResponse) {
        ValueSchema value = new ValueSchema(
            "On/off",
            "rw",
            new Number(
                0,
                1,
                1,
                "Boolean"
            )
        );
        LinkedList<ValueSchema> values = new LinkedList<>();
        values.add(value);

        DeviceSchema device = new DeviceSchema(
            "Switch",
            values
        );

        LinkedList<DeviceSchema> devices = new LinkedList<>();
        devices.add(device);
        return new NetworkSchema(
            "On/off switch",
            creatorResponse.network.id,
            devices
        );
    }
}
