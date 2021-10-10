package actions.wapps;

import actions.*;
import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.api.rest.wapps.*;
import wappsto.api.wapps.*;

import static actions.Utils.*;

@Action(name = "Install wapp")
public class InstallWapp implements WebAction {

    @Parameter(
        description = "Service API root",
        defaultValue = Defaults.SERVICE_URL
    )
    public String serviceUrl;

    @Parameter(description = "Name of wapp to install")
    public String nameOfWapp;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId = getSessionFrom(browser);

        new Controller(sessionId, serviceUrl, nameOfWapp).execute();

        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private WappService service;
        private String nameOfWapp;

        public Controller(
            String sessionId,
            String serviceUrl,
            String nameOfWapp
        )
            throws FailureException
        {
            this(
                new RestWappService(createRestSession(sessionId, serviceUrl)),
                nameOfWapp
            );
        }

        public Controller(WappService service, String nameOfWapp) {
            this.service = service;
            this.nameOfWapp = nameOfWapp;
        }

        public void execute() throws FailureException {
            try {
                service.install(nameOfWapp);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to install Wapp: " + e.getMessage()
                );
            }
        }
    }
}
