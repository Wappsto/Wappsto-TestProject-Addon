package actions.session;

import io.testproject.java.annotations.v2.Parameter;

public abstract class ActionWithAdminSession {
    @Parameter(
        description = "Service API URL",
        defaultValue = "https://qa.wappsto.com/services"
    )
    public String serviceUrl;

    @Parameter(description = "Admin username")
    public String adminUsername;

    @Parameter(description = "Admin password")
    public String adminPassword;
}
