package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.from.server.*;

import java.io.*;

public class JsonRPCParser {
    private final ObjectMapper mapper;
    private final SuccessResponseStrategy successResponse;
    private final ResultStrategy result;
    private final UpdateStateStrategy updateState;

    public JsonRPCParser(
        ResultStrategy result,
        UpdateStateStrategy updateState,
        SuccessResponseStrategy successResponse
    ) {
        this.result = result;
        this.updateState = updateState;
        this.successResponse = successResponse;
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
                    default:
                        break;
                }

            } else {
                result.execute(
                    new ResponseData(
                        mapper.readValue(data, RpcResult.class)
                    )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCommand(JsonNode node) {
        return node.get("result") == null;
    }

    private void executeStateCommand(String data) throws IOException {
        RpcStateCommand command = mapper
            .readValue(data, RpcStateCommand.class);
        successResponse.execute(
            new SuccessResponseToServer(command.id)
        );
        updateState.execute(
            new ControlStateData(
                command.params.data.meta.id,
                command.params.data.data
            )
        );
    }
}
