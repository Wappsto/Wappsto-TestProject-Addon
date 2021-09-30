package actions.session;

import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

public class CreateNewUserController {
    private final User user;

    public CreateNewUserController(
        AdminCredentials adminCredentials,
        Credentials userCredentials,
        String target
    )
        throws Exception
    {
        this(
            new RestAdmin(adminCredentials, target),
            RestUser.class,
            userCredentials,
            target);
    }

    public CreateNewUserController(
        Admin admin,
        Class<? extends User> U,
        Credentials credentials,
        String target
    )
        throws Exception
    {
        admin.register(credentials);
        //evil dependencty injection hack using reflection
        user = U.getConstructor(Credentials.class, String.class)
            .newInstance(credentials, target);
    }

    public String execute() {
        return user.getId();
    }
}
