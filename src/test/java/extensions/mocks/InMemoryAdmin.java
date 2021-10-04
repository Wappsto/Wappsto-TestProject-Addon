package extensions.mocks;

import wappsto.session.*;
import wappsto.session.model.*;

import java.util.*;

public class InMemoryAdmin implements Admin {
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
        fetchUser(username);
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
