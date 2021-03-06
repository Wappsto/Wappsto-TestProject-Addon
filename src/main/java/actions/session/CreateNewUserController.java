package actions.session;

import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;
import wappsto.api.session.model.*;

import static actions.Utils.*;

public class CreateNewUserController {
    private final User user;

    public CreateNewUserController(
        AdminCredentials adminCredentials,
        Credentials userCredentials,
        String target
    )
        throws FailureException
    {
        this(
            createRestAdminSession(adminCredentials, target),
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
        throws FailureException
    {
        try {
            admin.register(credentials);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to register user: " + e.getMessage()
            );
        }
        //evil dependency injection hack using reflection
        try {
            user = U.getConstructor(Credentials.class, String.class)
                .newInstance(credentials, target);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to create user session: " + e.getMessage() + "\n" +
                    "Have you implemented the constructor '(Credentials, String)'?"
            );
        }
    }

    public String execute() {
        return user.getId();
    }
}
