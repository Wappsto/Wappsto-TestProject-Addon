package actions.wapps;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.WebDriver;
import wappsto.rest.wapps.RestWappService;
import wappsto.rest.session.RestUser;
import wappsto.session.*;
import wappsto.wapps.*;

import static actions.Utils.createRestSession;
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
