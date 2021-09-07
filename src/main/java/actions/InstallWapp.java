package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.WebDriver;
import wappsto.rest.session.UserSession;

@Action(name = "Install wapp")
public class InstallWapp implements WebAction {

    @Parameter(description = "Service API root")
    public String serviceUrl;

    @Parameter(description = "Name of wapp to install")
    public String nameOfWapp;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId = browser
            .manage()
            .getCookieNamed("sessionId")
            .getValue();

        UserSession session;
        try {
            session = new UserSession(
                sessionId,
                serviceUrl
            );
            session.install(nameOfWapp);
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }

        return ExecutionResult.PASSED;
    }
}
