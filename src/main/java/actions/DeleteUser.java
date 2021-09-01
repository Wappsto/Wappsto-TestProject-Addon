package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
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
            e.printStackTrace();
            return ExecutionResult.FAILED;
        }
        return ExecutionResult.PASSED;
    }
}
