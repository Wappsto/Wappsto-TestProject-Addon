package acceptance;

import wappsto.api.network.model.*;
import wappsto.api.rest.network.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;

import java.util.*;

import static util.Env.*;

public class Util {
    public static NetworkSchema createSavedNetwork(
        NetworkSchema schema,
        DataStore store,
        RestNetworkService service
    )
        throws Exception
    {
        CreatorResponse creator = service.getCreator(true);
        NetworkInstance instance = new NetworkInstance(
            new WappstoCerts(creator),
            schema
        );
        schema.meta.id = UUID.fromString(creator.network.id);
        SSLConnection connection = new SSLConnection(
            env().get(SOCKET_URL),
            Integer.parseInt(env().get(SOCKET_PORT)),
            new WappstoCerts(creator)
        );
        new VirtualIoTNetwork(schema, new RpcClient(connection));
        store.save(schema.meta.id.toString(), instance);
        return schema;
    }
}
