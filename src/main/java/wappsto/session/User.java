package wappsto.session;

import wappsto.session.model.*;

public interface User {
    UserResponse fetchUser() throws Exception;
}
