package extensions.injectors;

import extensions.mocks.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.model.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.session.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;

import java.util.*;

import static util.Env.*;
import static util.Utils.*;

public class VirtualIoTNetworkInjector implements ParameterResolver {
    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return parameterContext
            .getParameter()
            .getType()
            .equals(VirtualIoTNetwork.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        NetworkSchema schema = createNetworkSchema();
        if (extensionContext.getTags().contains("unit")) {
            Connection connection = new InMemoryConnection();
            RpcClient client = new RpcClient(connection);
            try {
                return new VirtualIoTNetwork(schema, client);
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to start VirtualIoTNetwork" + e.getMessage()
                );
            }
        } else {
            RestAdmin admin = createNewAdmin();
            RestUser user = createNewUserSession(env().get(API_ROOT), admin);
            RestNetworkService service = new RestNetworkService(user);
            CreatorResponse creator;
            try {
                creator = service.getCreator();
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to get creator: " + e.getMessage()
                );
            }
            schema.meta.id = UUID.fromString(creator.network.id);
            RpcClient client;
            try {
                SSLConnection connection = new SSLConnection(
                    env().get(SOCKET_URL),
                    Integer.parseInt(env().get(SOCKET_PORT)),
                    new WappstoCerts(creator)
                );
                client = new RpcClient(connection);
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to establish SSL connection" + e.getMessage()
                );
            }
            try {
                return new VirtualIoTNetwork(schema, client);
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to start VirtualIoTNetwork" + e.getMessage()
                );
            }
        }


    }
}
