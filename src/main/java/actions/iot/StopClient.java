package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.iot.*;

import java.util.*;


public class StopClient implements GenericAction {
    @Parameter(description = "Network UUID")
    public String network;

    @Override
    public ExecutionResult execute(AddonHelper helper) throws FailureException {
        new Controller(network).execute();
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private String network;

        public Controller(String network) {
            this.network = network;
        }

        public void execute() throws FailureException {
            try {
                VirtualIoTNetworkStore
                    .getInstance()
                    .stop(UUID.fromString(network));
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to stop network: " + e.getMessage()
                );
            }
        }
    }
}
