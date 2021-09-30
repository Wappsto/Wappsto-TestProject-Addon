package iot;

import wappsto.iot.network.model.*;
import wappsto.network.model.*;

public class Utils {

    public static NetworkSchema defaultNetwork(
        CreatorResponse creatorResponse
    ) {
        return new NetworkSchema.Builder(
            "On/off switch",
            creatorResponse.network.id
        ).addDevice("Switch")
            .addValue("On/off", ValuePermission.RW)
            .withNumberSchema(0, 1, 1, "Boolean")
            .addToDevice()
            .addToNetwork()
            .build();
    }
}
