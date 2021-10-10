package unit.actions.session;

import actions.session.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.session.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(AdminInjector.class)
public class LogInAsAdminControllerTest {
    @Test
    public void fetches_admin_session(Admin admin) {
        LogInAsAdmin.Controller controller = new LogInAsAdmin.Controller(admin);
        assertNotNull(controller.execute());
    }
}
