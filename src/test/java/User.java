import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import wappsto.rest.AdminSession;
import io.testproject.sdk.interfaces.parameterization.TestProjectParameterizer;
import wappsto.rest.UserSession;

public class User {

    @Nested
    public class Given {
        AdminSession admin = AdminSession.instance();

        @ParameterizedTest
        @ArgumentsSource(TestProjectParameterizer.class)
        public void i_am_a_logged_in_user(String username, String password) {
            UserSession user = admin.register(username, password);


        }
    }
}
