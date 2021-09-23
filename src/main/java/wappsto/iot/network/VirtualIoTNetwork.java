package wappsto.iot.network;

import com.fasterxml.jackson.core.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;

import java.io.*;
import java.util.*;

import static wappsto.iot.rpc.Utils.toJson;

public class VirtualIoTNetwork {
    private final NetworkSchema schema;
    private final IoTClient client;
    private HashMap<UUID, Value> values;

    public VirtualIoTNetwork(NetworkSchema schema, IoTClient client)
        throws Exception
    {
        this.schema = schema;
        this.client = client;
        values = new HashMap<>();
        for (DeviceSchema d : schema.device) {
            for (ValueSchema v : d.value) {
                StateSchema control = v.state.stream()
                    .filter(s -> s.type.equals("Control"))
                    .findAny()
                    .orElseThrow();
                StateSchema report = v.state.stream()
                    .filter(s -> s.type.equals("Report"))
                    .findAny()
                    .orElseThrow();
                values.put(
                    control.meta.id,
                    new Value(report.meta.id, report.data)
                );
            }
        }

        try {
            client.start(new JsonRPCParser(null, this::update));
            client.send(toJson(schema));
        } catch (JsonProcessingException e) {
            throw new Exception("Schema error: " + e.getMessage());
        }
    }

    public void update(ControlStateData request) {
        values.get(request.state).value = request.data;
        ReportState report = new ReportState(
            "/state",
            new ReportData(request.data)
        );
        try {
            client.send(toJson(new RPCRequest(report, Methods.PUT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
