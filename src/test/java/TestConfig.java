import java.io.IOException;
import java.util.Properties;

public class TestConfig {
    public final String DEV_TOKEN;
    public final String ADMIN_USERNAME;
    public final String ADMIN_PASSWORD;
    public final String API_ROOT;
    public final String APP_URL;

    public TestConfig() throws IOException {
        Properties props = new Properties();

        props.load(
            getClass().
                getClassLoader().
                getResourceAsStream("test-secret.config"));
        ADMIN_USERNAME = props.getProperty("username");
        ADMIN_PASSWORD = props.getProperty("password");
        DEV_TOKEN = props.getProperty("dev_token");
        API_ROOT = props.getProperty("api_root");
        APP_URL = props.getProperty("app_url");
    }
}
