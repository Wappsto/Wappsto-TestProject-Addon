package unit.actions.session;

import actions.session.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.session.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.Utils.defaultUser;

@Tag("unit")
@ExtendWith(AdminInjector.class)
@ExtendWith(UserTypeInjector.class)
public class CreateNewUserControllerTest {
    @Test
    public void registers_new_user(Admin admin, Class<? extends User> T)
        throws FailureException
    {
        CreateNewUserController controller = new CreateNewUserController(
            admin,
            T,
            defaultUser(),
            "ignore"
        );

        assertNotNull(controller.execute());
    }
}
