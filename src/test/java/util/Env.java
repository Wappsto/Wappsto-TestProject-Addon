package util;

import java.util.Map;

public class Env {
    public static final String ADMIN_USERNAME = "WAPPSTO_ADMIN_USERNAME";
    public static final String ADMIN_PASSWORD = "WAPPSTO_ADMIN_PASSWORD";
    public static final String API_ROOT = "WAPPSTO_API_ROOT";
    public static final String ADMIN_PANEL = "WAPPSTO_ADMIN_PANEL_URL";
    public static final String DEV_TOKEN = "TESTPROJECT_DEV_TOKEN";
    public static final String APP_URL = "WAPPSTO_APP_URL";
    public static final String SOCKET_URL = "WAPPSTO_SOCKET_URL";
    public static final String SOCKET_PORT = "WAPPSTO_SOCKET_PORT";

    public static Map<String, String> env() {
        return  System.getenv();
    }
}
