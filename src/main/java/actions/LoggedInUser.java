package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.enums.ParameterDirection;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.*;
import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.model.Credentials;
import wappsto.rest.session.AdminSession;
import wappsto.rest.session.SessionBuilder;
import wappsto.rest.session.UserSession;

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
            session = new SessionBuilder(adminSession)
                .withCredentials(userCredentials)
                .create();

        } catch (Forbidden e) {
            e.printStackTrace();
        }

        driver.get("https://qa.wappsto.com");
        driver.manage().addCookie(new Cookie("sessionID", session.getId()));

        return ExecutionResult.PASSED;
    }
}
