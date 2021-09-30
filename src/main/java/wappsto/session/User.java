package wappsto.session;

import wappsto.session.model.*;

public interface User extends Session {

    /* So Java doesn't support multiple inheritance, which is why User
     * is an interface as opposed to an abstract class. This means I cannot
     * enforce the contract, that this constructor must exist. I use reflection
     * to instantiate a user object with two string parameters - if you
     * implement this interface, please implement the following constructor:
     *  User(String sessionId, String serviceUrl) throws Exception;
     */

    UserResponse fetchMe() throws Exception;
}
