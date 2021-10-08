package wappsto.iot.network;

import wappsto.iot.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.schema.outgoing.*;

import java.util.*;

import static wappsto.iot.rpc.Utils.*;

public class VirtualIoTNetwork {
    public final NetworkSchema schema;
    public final IoTClient client;
    private final HashMap<UUID, Value> values;
    private final List<UUID> controlStates;
    private final List<UUID> reportStates;

    public VirtualIoTNetwork(NetworkSchema schema, IoTClient client) {
        this.schema = schema;
        this.client = client;
        values = new HashMap<>();
        controlStates = new LinkedList<>();
        reportStates = new LinkedList<>();
        addStatesAndValues(schema);

        client.start(new RpcParser(
            new RpcStrategies(data -> {},
                this::update,
                response -> client.send(toJson(response)),
                null))
        );
        client.send(toJson(
            new RpcRequest(new Params("/network", schema), Methods.POST))
        );
    }

    public void update(ControlStateData request) {
        values.get(request.state).value = request.data;
        ReportState report = new ReportState(
            values.get(request.state).reportState,
            new ReportData(request.data)
        );

        client.send(toJson(new RpcRequest(report, Methods.PUT)));
    }

    public UUID getControlState(int index) {
        return controlStates.get(index);
    }

    public UUID getReportState(int index) {
        return reportStates.get(index);
    }

    public void stop() {
        client.stop();
    }

    private void addStatesAndValues(NetworkSchema schema) {
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
    }
}
