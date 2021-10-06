package actions.session;

import actions.*;
import io.testproject.java.annotations.v2.*;

public abstract class ActionWithAdminSession {
    @Parameter(
        description = "Service API URL",
        defaultValue = Defaults.SERVICE_URL
    )
    public String serviceUrl;

    @Parameter(description = "Admin username")
    public String adminUsername;

    @Parameter(description = "Admin password")
    public String adminPassword;
}
