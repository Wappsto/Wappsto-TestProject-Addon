package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.from.server.*;

public class JsonRPCParser {
    private final SuccessResponse successResponse;
    private final ObjectMapper mapper;
    private final ServerReponse responseFromServer;
    private final ControlState controlState;

    public JsonRPCParser(
        ServerReponse responseFromServer,
        ControlState controlState,
        SuccessResponse successResponse
    ) {
        this.responseFromServer = responseFromServer;
        this.controlState = controlState;
        this.successResponse = successResponse;
        mapper = new ObjectMapper();
    }

    public void parse(String data) {
        try {
            JsonNode node = mapper.readTree(data);

            if (node.get("result") == null) {
                JsonRPCRequestFromServer request = mapper
                    .readValue(data, JsonRPCRequestFromServer.class);
                successResponse.execute(
                    new SuccessResponseToServer(request.id)
                );
                controlState.execute(
                    new ControlStateData(
                        request.params.data.meta.id,
                        request.params.data.data
                    )
                );
            } else {
                responseFromServer.execute(
                    new ResponseData(
                        mapper.readValue(data, JsonRPCResponse.class)
                    )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
