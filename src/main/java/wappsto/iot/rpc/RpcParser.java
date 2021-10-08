package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.schema.*;
import wappsto.iot.rpc.model.schema.incoming.*;
import wappsto.iot.rpc.model.schema.outgoing.*;

import java.io.*;

public class RpcParser {
    private final ObjectMapper mapper;
    private final SuccessResponseStrategy successResponse;
    private final ResultStrategy result;
    private final UpdateStateStrategy updateState;
    private final DeleteStrategy deleteCommand;

    public RpcParser(RpcStrategies rpcStrategies) {
        this.result = rpcStrategies.result;
        this.updateState = rpcStrategies.updateState;
        this.successResponse = rpcStrategies.successResponse;
        this.deleteCommand = rpcStrategies.delete;
        mapper = new ObjectMapper();
    }

    public void parse(String data) {
        try {
            JsonNode node = mapper.readTree(data);

            if (isCommand(node)) {
                RpcBase rpc = mapper.readValue(data, RpcBase.class);
                switch (rpc.method) {
                    case PUT:
                        executeStateCommand(data);
                    case DELETE:
                        executeDeleteCommand();
                    default:
                        break;
                }

            } else {
                result.execute(
                    new ResponseData(
                        mapper.readValue(data, RpcIncomingResult.class)
                    )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeDeleteCommand() {
        deleteCommand.execute();
    }

    private boolean isCommand(JsonNode node) {
        return node.get("result") == null;
    }

    private void executeStateCommand(String data) throws IOException {
        RpcStateCommand command = mapper
            .readValue(data, RpcStateCommand.class);
        successResponse.execute(
            new RpcOutgoingResult(command.id)
        );
        updateState.execute(
            new ControlStateData(
                command.params.data.meta.id,
                command.params.data.data
            )
        );
    }
}
