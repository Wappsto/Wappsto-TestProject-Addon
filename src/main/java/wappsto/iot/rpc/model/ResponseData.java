package wappsto.iot.rpc.model;

import wappsto.iot.rpc.model.from.server.*;

public class ResponseData {
    public String id;

    public ResponseData(RpcResult data) {
        this.id = data.id;
    }
}
