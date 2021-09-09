package util;

import java.util.Map;

public class Env {
    public static final String ADMIN_USERNAME = "WAPPSTO_ADMIN_USERNAME";
    public static final String ADMIN_PASSWORD = "WAPPSTO_ADMIN_PASSWORD";
    public static final String API_ROOT = "WAPPSTO_API_ROOT";
    public static final String ADMIN_PANEL = "WAPPSTO_ADMIN_PANEL_URL";
    public static final String DEV_TOKEN = "TESTPROJECT_DEV_TOKEN";
    public static final String APP_URL = "WAPPSTO_APP_URL";
    public static final String NETWORK_TOKEN = "WAPPSTO_TEST_NETWORK";
    public static final String DEVELOPER_USERNAME = "WAPPSTO_DEVELOPER_USERNAME";
    public static final String DEVELOPER_PASSWORD = "WAPPSTO_DEVELOPER_PASSWORD";

    public static Map<String, String> env() {
        return  System.getenv();
    }
}
