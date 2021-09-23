package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.from.server.*;

public class JsonRPCParser {
    ObjectMapper mapper;
    ServerReponse responseFromServer;
    ControlState controlState;

    public JsonRPCParser(ServerReponse responseFromServer, ControlState controlState) {
        this.responseFromServer = responseFromServer;
        this.controlState = controlState;
        mapper = new ObjectMapper();
    }

    public void parse(String data) {
        try {
            JsonNode node = mapper.readTree(data);

            if (node.get("result") == null) {
                controlState.execute(
                    new ControlStateData(
                        mapper.readValue(data, JsonRPCRequestFromServer.class)
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
