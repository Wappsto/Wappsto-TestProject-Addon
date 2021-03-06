package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(NetworkServiceInjector.class)
public class ChangeControlStateControllerTest {

    @Test
    public void updates_specified_control_state(NetworkService service)
        throws Exception
    {
        UUID state = UUID.randomUUID();
        String expected = "value";
        ChangeControlState.Controller controller = new ChangeControlState
            .Controller(service, state.toString(), expected);
        controller.execute();
        assertEquals(expected, service.getState(state));
    }
}
