package actions.session;

import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

public class DeleteUserController {
    private Admin admin;
    private String username;

    public DeleteUserController(
        AdminCredentials credentials,
        String target,
        String username
    ) throws Exception
    {
        this(new RestAdmin(credentials, target), username);
    }

    public DeleteUserController(Admin admin, String username) {
        this.admin = admin;
        this.username = username;
    }

    public void execute() throws Exception {
        admin.delete(username);
    }
}
