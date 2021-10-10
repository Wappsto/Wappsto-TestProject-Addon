package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(NetworkServiceInjector.class)
public class AssertStateValueControllerTest {

    @Nested
    public class throws_when {
        @Test
        public void state_does_not_match(NetworkService service)
            throws Exception
        {
            UUID state = UUID.randomUUID();
            service.updateState(state, "new value");
            AssertStateValue.Controller controller = new AssertStateValue
                .Controller(service, state.toString(), "different value");
            assertThrows(FailureException.class, () -> controller.execute());
        }

        @Test
        public void state_does_not_exist(NetworkService service) {
            UUID state = UUID.randomUUID();
            AssertStateValue.Controller controller = new AssertStateValue
                .Controller(service, state.toString(), "value");

            assertThrows(FailureException.class, () -> controller.execute());
        }
    }



    @Test
    public void does_not_throw_when_state_matches(NetworkService service)
        throws Exception
    {
        UUID state = UUID.randomUUID();
        String expected = "value";
        service.updateState(state, expected);
        AssertStateValue.Controller controller = new AssertStateValue
            .Controller(service, state.toString(), expected);
        assertDoesNotThrow(() -> controller.execute());
    }
}
