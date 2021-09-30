package actions.iot;

import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

import java.util.*;

public class AssertStateValueController {
    public final NetworkService service;
    private final String state;
    private final String expected;

    public AssertStateValueController(
        String sessionId,
        String target,
        String state,
        String expected
    )
        throws Exception
    {
        this(
            new RestNetworkService(new RestUser(sessionId, target)),
            state,
            expected
        );
    }

    public AssertStateValueController(
        NetworkService service,
        String state,
        String expected
    ) {
        this.service = service;
        this.state = state;
        this.expected = expected;
    }

    public void execute() throws Exception {
        assert service
            .getState(UUID.fromString(state))
            .equals(expected) : "State did not match expected value";
    }
}
