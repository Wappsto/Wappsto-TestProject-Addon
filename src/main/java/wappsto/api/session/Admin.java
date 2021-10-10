package wappsto.api.session;

import wappsto.api.session.model.*;

public interface Admin extends Session {
    void register(Credentials credentials) throws Exception;

    void delete(String username) throws Exception;

}
