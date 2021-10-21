package wappsto.iot.network;

import wappsto.iot.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.schema.outgoing.*;

import java.util.*;

import static wappsto.iot.rpc.Utils.*;

/**
 * Simulated IoT device. Keeps track of the internal state of the device
 */
public class VirtualIoTNetwork {
    public final NetworkSchema schema;
    public final IoTClient client;
    private final HashMap<UUID, UUID> controlAndReportStates;
    private final HashMap<UUID, String> reportValues;
    private final List<UUID> controlStates;
    private final List<UUID> reportStates;
    private boolean isRunning;

    /**
     * Instantiate the network
     * @param schema internal data structure of the network containing devices
     *               values and states
     * @param client Client between the network and the conneciton
     * @throws Exception
     */
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

    /**
     * Updates a report state via an incoming control state request from the
     * server. This network is designed to be an echo network, so it simply sets
     * the report state to the value of the control state
     * @param request update request with a state UUID and the desired value
     */
    public void updateControlState(StateData request) {
        request.state = controlAndReportStates.get(request.state);
        try {
            updateReportState(request);
        } catch (Exception e) {
            throw new RuntimeException("Report state does not exist");
        }
    }

    /**
     * Update a report state directly
     * @param request
     * @throws Exception
     */
    public void updateReportState(StateData request) throws Exception {
        if (!reportValues.containsKey(request.state)) {
            throw new Exception("Invalid report state");
        }
        reportValues.put(request.state, request.data);
        ReportState report = new ReportState(
            request.state,
            new ReportData(reportValues.get(request.state))
        );

        schema.device.stream()
            .flatMap( d -> d.value.stream() )
            .flatMap( v -> v.state.stream() )
            .filter( s -> s.meta.id.equals(request.state) )
            .findAny()
            .ifPresent( s -> s.data = request.data );

        client.send(toJson(new RpcRequest(report, Methods.PUT)));
    }

    /**
     * Return the UUID of a control state from a list
     * @param index
     * @return control state UUID
     */
    public UUID getControlStateId(int index) {
        return controlStates.get(index);
    }

    /**
     * Return the UUID of a report state from a list
     * @param index
     * @return report state UUID
     */
    public UUID getReportStateId(int index) {
        return reportStates.get(index);
    }

    /**
     * Stop the network
     */
    public void stop() {
        client.stop();
        isRunning = false;
    }

    /**
     * Check the status of the network
     * @return true: network is running
     *         false: network is stopped
     */
    public boolean isRunning() {
        return isRunning;
    }

    private void addStatesAndValues(NetworkSchema schema) {
        for (DeviceSchema d : schema.device) {
            for (ValueSchema v : d.value) {

                Optional<StateSchema> report = v.state.stream()
                    .filter(s -> s.type.equals("Report"))
                    .findAny();

                report.ifPresent(s -> {
                    reportStates.add(s.meta.id);
                    reportValues.put(s.meta.id, s.data);
                });

                v.state.stream()
                    .filter(s -> s.type.equals("Control"))
                    .findAny()
                    .ifPresent(s -> {
                        report.ifPresent(
                            stateSchema -> controlAndReportStates.put(
                                s.meta.id,
                                stateSchema.meta.id
                            ));
                        controlStates.add(s.meta.id);
                    });
            }
        }
    }
}
