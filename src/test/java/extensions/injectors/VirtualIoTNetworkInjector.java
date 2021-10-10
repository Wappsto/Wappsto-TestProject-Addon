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
            return new VirtualIoTNetwork(schema, client);
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
                client = new RpcClient.Builder(creator)
                    .connectingTo(
                        env().get(SOCKET_URL),
                        Integer.parseInt(env().get(SOCKET_PORT))
                    ).build();
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to establish SSL connection" + e.getMessage()
                );
            }
            return new VirtualIoTNetwork(schema, client);
        }


    }
}
