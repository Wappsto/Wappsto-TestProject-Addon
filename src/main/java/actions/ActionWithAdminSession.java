package actions;

import io.testproject.java.annotations.v2.Parameter;

public abstract class ActionWithAdminSession {
    @Parameter(
        description = "Application API URL",
        defaultValue = "https://qa.wappsto.com/services/2.1/"
    )
    public String serviceUrl;

    @Parameter(description = "Admin username")
    public String adminUsername;

    @Parameter(description = "Admin password")
    public String adminPassword;
}
