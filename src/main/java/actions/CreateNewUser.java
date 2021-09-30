package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.session.model.*;

import static actions.Utils.registerNewUser;

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
        registerNewUser(adminCredentials, userCredentials, serviceUrl);
        return ExecutionResult.PASSED;
    }
}
