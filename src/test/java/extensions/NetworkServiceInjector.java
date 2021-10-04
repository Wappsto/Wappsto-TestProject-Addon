package extensions;

import org.junit.jupiter.api.extension.*;
import wappsto.network.*;
import wappsto.network.model.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;
import wappsto.session.model.*;

import java.util.*;

import static util.Env.*;
import static util.Utils.createNewUserSession;
import static util.Utils.defaultUser;

public class NetworkServiceInjector implements ParameterResolver {

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
            .equals(NetworkService.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        if (extensionContext.getTags().contains("unit")) {
            return new InMemoryNetworkService();
        } else {
            try {
                RestAdmin admin = new RestAdmin(
                    new AdminCredentials(
                        env().get(ADMIN_USERNAME),
                        env().get(ADMIN_PASSWORD)
                    ),
                    env().get(API_ROOT)
                );
                RestUser session = createNewUserSession(
                    env().get(API_ROOT),
                    admin
                );
                return new RestNetworkService(session);
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to register user: " + e.getMessage()
                );
            }
        }
    }

    private static class InMemoryNetworkService implements NetworkService {
        private HashMap<UUID, String> states;
        private HashMap<UUID, NetworkMeta> networks;

        public InMemoryNetworkService() {
            states = new HashMap<>();
            networks = new HashMap<>();
        }

        @Override
        public void claim(String id) throws Exception {
            if (networks.get(UUID.fromString(id)) == null) {
                throw new Exception("Network not found");
            }
        }

        @Override
        public CreatorResponse getCreator() throws Exception {
            return null;
        }

        @Override
        public NetworkMeta create() throws Exception {
            NetworkMeta meta = new NetworkMeta();
            UUID id = UUID.randomUUID();
            meta.id = id.toString();
            networks.put(id, meta);
            return meta;
        }

        @Override
        public NetworkMeta fetch(String id) throws Exception {
            return null;
        }

        @Override
        public void share(NetworkMeta network, UserResponse friend) throws Exception {

        }

        @Override
        public void share(String networkId, String friendUsername) throws Exception {

        }

        @Override
        public void updateState(UUID id, String data) throws Exception {
            states.put(id, data);
        }

        @Override
        public String getState(UUID id) throws Exception {
            String value = states.get(id);
            if (value == null) throw new Exception("Not found");
            else return value;
        }
    }
}
