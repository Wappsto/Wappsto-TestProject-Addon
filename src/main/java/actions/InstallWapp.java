package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.WebDriver;
import wappsto.rest.session.User;

import static actions.Utils.getSessionFrom;

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
        String sessionId = getSessionFrom(browser);

        User session;
        try {
            session = new User(
                sessionId,
                serviceUrl
            );
            session.install(nameOfWapp);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to install wapp named: "
                    + nameOfWapp + ". "
                    + e.getMessage()
            );
        }

        return ExecutionResult.PASSED;
    }
}