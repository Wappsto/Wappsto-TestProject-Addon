package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.*;

public class JsonRPCParser {
    ObjectMapper mapper;

    public JsonRPCParser() {
        mapper = new ObjectMapper();
    }

    public JsonRPCMessage parse(String data) throws Exception {
        JsonNode node = mapper.readTree(data);

        if (node.get("result") == null) {
            return mapper.readValue(data, JsonRPCRequestFromServer.class);
        } else {
            return mapper.readValue(data, JsonRPCResponse.class);
        }
    }
}
