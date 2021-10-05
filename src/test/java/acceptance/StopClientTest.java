package acceptance;

import actions.iot.*;
import extensions.injectors.*;
import io.testproject.java.execution.results.*;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.network.*;
import wappsto.rest.request.exceptions.*;
import wappsto.session.*;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utils.*;


@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(VirtualIoTNetworkInjector.class)
@ExtendWith(AdminInjector.class)
public class StopClientTest {

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        deleteUser(admin);
    }

    @Test
    public void fails_to_stop_nonexistent_client() {

        StopClient action = new StopClient();
        action.network = UUID.randomUUID().toString();

        assertThrows(
            ExecutionException.class,
            () -> runner().run(action)
        );
    }

    @Test
    public void stops_running_client(VirtualIoTNetwork network)
        throws Exception
    {
        StopClient action = new StopClient();
        action.network = network.schema.meta.id.toString();

        StepExecutionResult run = runner().run(action);
        assertEquals(
            ExecutionResult.PASSED.name().toUpperCase(),
            run.getResultType().name().toUpperCase()
        );
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        resetRunner();
        deleteUser(admin);
    }

    private static void deleteUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
