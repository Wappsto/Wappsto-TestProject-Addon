package unit.actions.wapps;

import actions.wapps.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.wapps.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(WappServiceInjector.class)
public class InstallWappControllerTest {
    @Test
    public void fails_to_install_nonexistent_wapp(WappService service) {
        InstallWapp.Controller controller = new InstallWapp
            .Controller(service, "nonexistent wapp");
        assertThrows(FailureException.class, () -> controller.execute());
    }

    @Test
    public void installs_wapp(WappService service) {
        InstallWapp.Controller controller = new InstallWapp
            .Controller(service, "wapp");

        assertDoesNotThrow(() -> controller.execute());
    }
}
