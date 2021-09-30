package actions.session;

import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

public class LogInExistingUserController {
    private User user;

    public LogInExistingUserController(
        Credentials credentials,
        String target
    ) throws Exception {
        this(
            new RestUser(credentials, target)
        );
    }

    public LogInExistingUserController(User user) {
        this.user = user;
    }

    public String execute() {
        return user.getId();
    }
}
