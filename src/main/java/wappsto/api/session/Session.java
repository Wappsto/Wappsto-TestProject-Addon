package wappsto.api.session;

import wappsto.api.session.model.*;

public interface Session {
    UserResponse fetchUser(String username) throws Exception;
    String getId();
}
