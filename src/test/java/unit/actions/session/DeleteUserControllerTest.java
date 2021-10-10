package unit.actions.session;

import actions.session.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.session.*;
import wappsto.api.session.model.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(AdminInjector.class)
public class DeleteUserControllerTest {
    @Test
    public void fails_to_delete_nonexistent_user(Admin admin) {
        DeleteUser.Controller controller = new DeleteUser
            .Controller(admin, "nonexistent");
        assertThrows(FailureException.class, controller::execute);
    }

    @Test
    public void deletes_user(Admin admin) throws Exception {
        admin.register(new Credentials("user", "123"));
        DeleteUser.Controller controller = new DeleteUser
            .Controller(admin, "user");
        assertDoesNotThrow(controller::execute);
    }
}
