package wappsto.iot.network;

import com.fasterxml.jackson.core.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;

import java.io.*;
import java.util.*;

import static wappsto.iot.rpc.Utils.*;

public class VirtualIoTNetwork {
    public final NetworkSchema schema;
    public final IoTClient client;
    private HashMap<UUID, Value> values;
    private List<UUID> controlStates;
    private List<UUID> reportStates;

    public VirtualIoTNetwork(NetworkSchema schema, IoTClient client) {
        this.schema = schema;
        this.client = client;
        values = new HashMap<>();
        controlStates = new LinkedList<>();
        reportStates = new LinkedList<>();
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
                controlStates.add(control.meta.id);
                reportStates.add(report.meta.id);
                values.put(
                    control.meta.id,
                    new Value(report.meta.id, report.data)
                );
            }
        }

        client.start(new JsonRPCParser(
            data -> {},
            this::update,
            response -> client.send(toJson(response)))
        );
        client.send(toJson(
            new RPCRequest(new Params("/network", schema), Methods.POST))
        );

    }

    public void update(ControlStateData request) {
        values.get(request.state).value = request.data;
        ReportState report = new ReportState(
            values.get(request.state).reportState,
            new ReportData(request.data)
        );

        client.send(toJson(new RPCRequest(report, Methods.PUT)));
    }

    public UUID getControlState(int index) {
        return controlStates.get(index);
    }

    public UUID getReportState(int index) {
        return reportStates.get(index);
    }
}
