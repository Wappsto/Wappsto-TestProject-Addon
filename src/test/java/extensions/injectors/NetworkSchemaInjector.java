package extensions.injectors;

import org.junit.jupiter.api.extension.*;
import wappsto.iot.network.model.*;

import java.util.*;

public class NetworkSchemaInjector implements ParameterResolver {

    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return parameterContext
            .getParameter()
            .getType()
            .equals(NetworkSchema.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return new NetworkSchema.Builder(
            "On/off switch",
            UUID.randomUUID().toString()
        ).addDevice("Switch")
            .addValue("On/off", ValuePermission.RW)
            .withNumberSchema(0, 1, 1, "Boolean")
            .addToDevice()
            .addToNetwork()
            .build();
    }
}
