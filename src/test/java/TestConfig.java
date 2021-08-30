import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class TestConfig {
    public final String ADMIN_USERNAME;
    public final String ADMIN_PASSWORD;

    public TestConfig() throws IOException {
        Properties props = new Properties();

        props.load(
            getClass().
                getClassLoader().
                getResourceAsStream("test-secret.config"));
        ADMIN_USERNAME = props.getProperty("username");
        ADMIN_PASSWORD = props.getProperty("password");
    }
}
