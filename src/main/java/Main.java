import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.request.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.request.Number;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;
import wappsto.rest.network.*;
import wappsto.rest.network.model.*;
import wappsto.rest.session.*;
import wappsto.rest.session.model.*;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        User session = new User(
            new Credentials(
                "lucca@seluxit.com",
                "123"
            ), "https://qa.wappsto.com/services/2.1"
        );
        NetworkService networkService = new NetworkService(session);
        CreatorResponse creatorResponse = networkService.getCreator();
        WappstoCerts certs = new WappstoCerts(creatorResponse);
        SSLConnection connection = new SSLConnection(
            "qa.wappsto.com",
            53005,
            certs
        );
        RPCClient client = new RPCClient(connection);

        Value value = new Value(
            "On/off",
            "rw",
            new Number(
                0,
                1,
                1,
                "Boolean"
            )
        );
        LinkedList<Value> values = new LinkedList<>();
        values.add(value);

        Device device = new Device(
            "Switch",
            values
        );

        LinkedList<Device> devices = new LinkedList<>();
        devices.add(device);
        RPCData data = new RPCData(
            "On/off switch",
            devices,
            creatorResponse.network.id
        );

        Params params = new Params(
            "/network",
            data
        );

        VirtualNetwork network = new VirtualNetwork(
            params,
            "POST",
            "thingy_1"
        );
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(network));
        client.send(mapper.writeValueAsString(network));
        //System.exit(0);
    }
}
