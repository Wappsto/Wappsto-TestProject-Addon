package extensions;

import org.junit.jupiter.api.extension.*;
import util.*;
import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

import java.util.*;

import static util.Env.API_ROOT;
import static util.Env.env;

public class AdminInjector implements ParameterResolver {

    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType().equals(Admin.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        if (extensionContext.getTags().contains("unit")) {
            return new InMemoryAdmin();
        } else {
            try {
                return new RestAdmin(
                    new AdminCredentials(
                        env().get(Env.ADMIN_USERNAME),
                        env().get(Env.ADMIN_PASSWORD)
                    ),
                    env().get(API_ROOT)
                );
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to create admin session: " + e.getMessage()
                );
            }
        }

    }

    private static class InMemoryAdmin implements Admin {
        private final String sessionId;
        private HashMap<String, UserMeta> users;

        public InMemoryAdmin() {
            this.sessionId = UUID.randomUUID().toString();
            users = new HashMap<>();
        }

        @Override
        public void register(Credentials credentials) throws Exception {
            UserMeta meta = new UserMeta();
            meta.id = UUID.randomUUID().toString();
            users.put(credentials.username, meta);
        }

        @Override
        public void delete(String username) throws Exception {
            users.remove(username);
        }

        @Override
        public UserResponse fetchUser(String username) throws Exception {
            UserMeta meta = users.get(username);
            if (meta == null) throw new Exception("Not found");
            UserResponse response = new UserResponse();
            response.username = username;
            response.meta = meta;
            return response;
        }

        @Override
        public String getId() {
            return sessionId;
        }
    }
}
