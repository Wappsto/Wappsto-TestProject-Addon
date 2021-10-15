package actions.iot;

import io.testproject.java.annotations.v2.*;

public abstract class ActionWithSSLSocket {
    @Parameter(description = "Socket URL")
    public String socketUrl;
    @Parameter(description = "Socket port")
    public String port;
}
