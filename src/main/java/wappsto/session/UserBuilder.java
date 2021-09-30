package wappsto.session;

import wappsto.session.model.*;

public interface UserBuilder {
    UserBuilder withCredentials(Credentials credentials);
    User create() throws Exception;
}
