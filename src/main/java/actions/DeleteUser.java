package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.session.AdminSession;

@Action(name = "Delete user")
public class DeleteUser extends ActionWithAdminSession implements WebAction {

    @Parameter
    public String username;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        AdminCredentials credentials = new AdminCredentials(
            adminUsername,
            adminPassword
        );

        try {
            AdminSession admin = new AdminSession(
                credentials,
                serviceUrl
            );
            admin.delete(username);
        } catch (Exception e) {
            throw new FailureException("Failed to delete: " + e.getMessage());
        }
        return ExecutionResult.PASSED;
    }
}
