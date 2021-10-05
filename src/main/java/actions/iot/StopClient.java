package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;


public class StopClient implements GenericAction {
    @Parameter(description = "Network UUID")
    public String network;

    @Override
    public ExecutionResult execute(AddonHelper helper) throws FailureException {
        return null;
    }
}
