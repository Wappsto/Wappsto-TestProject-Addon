package wappsto.iot.rpc.model;

import wappsto.iot.rpc.model.schema.incoming.*;

public class ResponseData {
    public String id;

    public ResponseData(RpcIncomingResult data) {
        this.id = data.id;
    }
}
