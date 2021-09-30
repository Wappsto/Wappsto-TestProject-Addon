package wappsto.session;

import wappsto.session.model.*;

public interface Session {
    UserResponse fetchUser(String username) throws Exception;
}
