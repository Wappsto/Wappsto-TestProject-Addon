package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.session.AdminSession;

@Action(name = "Log in as admin")
public class LogInAsAdmin extends ActionWithAdminSession implements WebAction {

    @Parameter(description = "Admin Panel URL")
    public String adminPanel;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {

        String sessionId;
        try {
            sessionId = createNewAdminSession();
        } catch (Exception e) {
            throw new FailureException("Admin login failed: " + e.getMessage());
        }

        WebDriver browser = helper.getDriver();
        browser.navigate().to(adminPanel);

        storeSession(sessionId, (JavascriptExecutor) browser);

        return ExecutionResult.PASSED;
    }

    private String createNewAdminSession() throws Exception {
        return new AdminSession(
            new AdminCredentials(
                adminUsername,
                adminPassword
            ),
            serviceUrl
        ).getId();
    }

    private void storeSession(String sessionId, JavascriptExecutor driver) {
        JavascriptExecutor js = driver;
        js.executeScript(
            String.format(
                "window.localStorage.setItem('%s', '%s')", "sessionID",
                sessionId));
    }
}
