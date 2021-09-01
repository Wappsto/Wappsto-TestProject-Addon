package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.*;
import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.model.*;
import wappsto.rest.session.*;

@Action(name = "Create a new logged in user")
public class LoggedInUser implements WebAction {

    @Parameter(
        description = "Application API URL",
        defaultValue = "https://qa.wappsto.com/services/2.1/"
    )
    public String appUrl;

    @Parameter(description = "Admin username")
    public String adminUsername;

    @Parameter(description = "Admin password")
    public String adminPassword;

    @Parameter(description = "Username")
    public String username;

    @Parameter(description = "Password")
    public String password;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {

        WebDriver driver = helper.getDriver();
        UserSession session = null;
        try {
            AdminCredentials adminCredentials = new AdminCredentials(
                adminUsername,
                adminPassword);
            Credentials userCredentials = new Credentials(
                username,
                password
            );
            AdminSession adminSession = new AdminSession(adminCredentials);
            session = new UserSessionBuilder(adminSession)
                .withCredentials(userCredentials)
                .create();

        } catch (Exception e) {
            e.printStackTrace();
            return ExecutionResult.FAILED;
        }

        driver.get("https://qa.wappsto.com");
        driver.manage().addCookie(new Cookie("sessionID", session.getId()));

        return ExecutionResult.PASSED;
    }
}
