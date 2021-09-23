package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.from.server.*;

public class JsonRPCParser {
    ObjectMapper mapper;
    Command responseFromServer;
    Command controlState;

    public JsonRPCParser(Command responseFromServer, Command controlState) {
        this.responseFromServer = responseFromServer;
        this.controlState = controlState;
        mapper = new ObjectMapper();
    }

    public void parse(String data) throws Exception {
        JsonNode node = mapper.readTree(data);

        if (node.get("result") == null) {
            controlState.execute(
                mapper.readValue(data, JsonRPCRequestFromServer.class)
            );
        } else {
            responseFromServer.execute(
                mapper.readValue(data, JsonRPCResponse.class)
            );
        }
    }
}
