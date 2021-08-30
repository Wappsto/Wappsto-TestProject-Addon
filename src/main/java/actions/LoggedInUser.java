package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.enums.ParameterDirection;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.*;
import wappsto.rest.*;
import wappsto.rest.model.Credentials;

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

    @Parameter(description = "Generated username", direction = ParameterDirection.OUTPUT)
    public String username;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {

        WebDriver driver = helper.getDriver();
        UserSession session = new SessionBuilder(
            new AdminSession(new Credentials(adminUsername, adminPassword))
        ).create();

        driver.manage().addCookie(new Cookie("sessionID", session.getId()));

        return ExecutionResult.PASSED;
    }
}
