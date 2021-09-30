package actions.session;

import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

public class LogInAsAdminController {
    private final Admin admin;

    public LogInAsAdminController(AdminCredentials credentials, String target)
        throws Exception
    {
        this(new RestAdmin(credentials, target));
    }

    public LogInAsAdminController(Admin admin) {

        this.admin = admin;
    }

    public String execute() {
        return admin.getId();
    }
}
