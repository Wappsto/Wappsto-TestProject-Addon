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

    @Override
    public ExecutionResult execute(WebAddonHelper helper)
        throws FailureException
    {
        AdminCredentials adminCredentials = new AdminCredentials(
            adminUsername,
            adminPassword
        );

        Credentials userCredentials = new Credentials(
            username,
            password
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
            admin.register(userCredentials);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to register user: " + e.getMessage()
            );
        }
        return ExecutionResult.PASSED;
    }
}
