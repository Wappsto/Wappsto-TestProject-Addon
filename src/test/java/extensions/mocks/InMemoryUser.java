package extensions.mocks;

import wappsto.api.session.*;
import wappsto.api.session.model.*;

import java.util.*;

public class InMemoryUser implements User {
    private final String sessionId;
    private final String userId;
    private final String username;

    public InMemoryUser(Credentials credentials, String ignored) {
        username = credentials.username;
        this.sessionId = UUID.randomUUID().toString();
        this.userId = UUID.randomUUID().toString();
    }

    @Override
    public UserResponse fetchUser(String username) throws Exception {
        UserResponse response = new UserResponse();
        response.username = username;
        response.meta.id = UUID.randomUUID().toString();
        return response;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public UserResponse fetchMe() throws Exception {
        UserResponse response = new UserResponse();
        response.username = username;
        response.meta.id = userId;
        return response;
    }
}
