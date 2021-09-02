package actions;

import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.session.AdminSession;

public class LogInAsAdmin extends ActionWithAdminSession implements WebAction {

    @Parameter(description = "Admin Panel URL")
    public String adminPanel;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {
        try {
            String sessionId = new AdminSession(
                new AdminCredentials(
                    adminUsername,
                    adminPassword
                ),
                serviceUrl
            ).getId();

            WebDriver driver = helper.getDriver();
            driver.navigate().to(adminPanel);
            driver.manage().addCookie(
                new Cookie("sessionID", sessionId));

            return ExecutionResult.PASSED;

        } catch (Exception e) {
            e.printStackTrace();
            return ExecutionResult.FAILED;
        }
    }
}
