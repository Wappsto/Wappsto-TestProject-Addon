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
    private final HashMap<UUID, UUID> controlAndReportStates;
    private final HashMap<UUID, String> reportValues;
    private final List<UUID> controlStates;
    private final List<UUID> reportStates;
    private boolean isRunning;

    public VirtualIoTNetwork(NetworkSchema schema, IoTClient client)
        throws Exception
    {
        this.schema = schema;
        this.client = client;
        controlAndReportStates = new HashMap<>();
        reportValues = new HashMap<>();

        controlStates = new LinkedList<>();
        reportStates = new LinkedList<>();
        addStatesAndValues(schema);

        client.start(new RpcParser(
            new RpcStrategies(
                data -> {},
                this::updateControlState,
                response -> client.send(toJson(response)),
                this::stop))
        );
        client.send(toJson(
            new RpcRequest(new Params("/network", schema), Methods.POST))
        );
        isRunning = true;
    }

    public void updateControlState(StateData request) {
        request.state = controlAndReportStates.get(request.state);
        try {
            updateReportState(request);
        } catch (Exception e) {
            throw new RuntimeException("Report state does not exist");
        }
    }

    public void updateReportState(StateData request) throws Exception {
        if (!reportValues.containsKey(request.state)) {
            throw new Exception("Invalid report state");
        }
        reportValues.put(request.state, request.data);
        ReportState report = new ReportState(
            request.state,
            new ReportData(reportValues.get(request.state))
        );
        updateStateInSchema(request);
        client.send(toJson(new RpcRequest(report, Methods.PUT)));
    }

    private void updateStateInSchema(StateData request) {
        schema.device.get(0).value.get(0).state
            .stream()
            .filter(s -> s.meta.id.equals(request.state))
            .findAny()
            .orElseThrow()
            .data = request.data;
    }

    public UUID getControlStateId(int index) {
        return controlStates.get(index);
    }

    public UUID getReportStateId(int index) {
        return reportStates.get(index);
    }

    public void stop() {
        client.stop();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
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
                controlAndReportStates.put(control.meta.id, report.meta.id);
                reportValues.put(report.meta.id, report.data);
            }
        }
    }
}
