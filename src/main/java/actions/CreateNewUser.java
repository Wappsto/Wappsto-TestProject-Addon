package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.rest.session.*;
import wappsto.rest.session.model.*;

@Action(name = "Create new user")
public class CreateNewUser extends ActionWithAdminSession implements WebAction {
    @Parameter(description = "Username")
    public String username;

    @Parameter(description = "Password")
    public String password;

    @Parameter(description = "Application API URL")
    public String serviceUrl;

    @Override
    public ExecutionResult execute(WebAddonHelper helper)
        throws FailureException
    {
        AdminCredentials adminCredentials = new AdminCredentials(
            adminUsername,
            adminPassword
        );
        Admin admin;
        try {
            admin = new Admin(adminCredentials, serviceUrl);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to create admin session: " + e.getMessage()
            );
        }
        try {
            admin.register(new Credentials(username, password));
        } catch (Exception e) {
            throw new FailureException(
                "Failed to register user: " + e.getMessage()
            );
        }
        return ExecutionResult.PASSED;
    }
}
