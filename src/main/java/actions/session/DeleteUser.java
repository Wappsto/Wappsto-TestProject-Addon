package actions.session;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import wappsto.session.*;
import wappsto.session.model.AdminCredentials;
import wappsto.rest.session.RestAdmin;

@Action(name = "Delete user")
public class DeleteUser extends ActionWithAdminSession implements WebAction {

    @Parameter
    public String username;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        try {
            new Controller(
                new AdminCredentials(adminUsername, adminPassword),
                serviceUrl,
                username
            ).execute();
        } catch (Exception e) {
            throw new FailureException("Failed to delete: " + e.getMessage());
        }
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private Admin admin;
        private String username;

        public Controller(
            AdminCredentials credentials,
            String target,
            String username
        ) throws Exception
        {
            this(new RestAdmin(credentials, target), username);
        }

        public Controller(Admin admin, String username) {
            this.admin = admin;
            this.username = username;
        }

        public void execute() throws Exception {
            admin.delete(username);
        }
    }
}
