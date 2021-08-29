package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import wappsto.rest.SessionBuilder;
import wappsto.rest.UserSession;

@Action(name = "Create a new logged in user")
public class LoggedInUser implements WebAction {

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {

        WebDriver driver = helper.getDriver();
        UserSession session = new SessionBuilder()
            .create();

        driver.manage().addCookie(new Cookie("sessionID", session.id));

        return ExecutionResult.PASSED;
    }
}
