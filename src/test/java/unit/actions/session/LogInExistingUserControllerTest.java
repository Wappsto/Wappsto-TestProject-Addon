package unit.actions.session;

import actions.session.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.session.*;
import wappsto.session.model.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(UserInjector.class)
public class LogInExistingUserControllerTest {
    @Test
    public void fetches_session_id(User user) throws Exception {
        LogInExistingUser.Controller controller = new LogInExistingUser
            .Controller(user);

        assertNotNull(controller.execute());
    }
}
