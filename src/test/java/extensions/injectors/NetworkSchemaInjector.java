package extensions.injectors;

import org.junit.jupiter.api.extension.*;
import wappsto.iot.network.model.*;

import static util.Utils.*;

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
        return createNetworkSchema();
    }
}
