package wappsto.session;

import wappsto.session.model.*;

public interface Admin {
    void register(Credentials credentials) throws Exception;

    void delete(String username) throws Exception;
}
