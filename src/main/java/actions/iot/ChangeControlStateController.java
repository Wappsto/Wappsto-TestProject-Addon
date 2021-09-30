package actions.iot;

import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

import java.util.*;

public class ChangeControlStateController {
    private final NetworkService service;
    private final String controlState;
    private final String value;

    public ChangeControlStateController(
        String sessionId,
        String target,
        String controlState,
        String value
    )
        throws Exception
    {
        this(
            new RestNetworkService(new RestUser(sessionId, target)),
            controlState,
            value
        );
    }

    public ChangeControlStateController(
        NetworkService service,
        String controlState,
        String value
    ) {
        this.service = service;
        this.controlState = controlState;
        this.value = value;
    }

    public void execute() throws Exception {
        service.updateState(UUID.fromString(controlState), value);
    }
}
