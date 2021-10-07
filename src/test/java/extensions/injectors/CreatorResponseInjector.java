package extensions.injectors;

import org.junit.jupiter.api.extension.*;
import wappsto.network.model.*;

import java.util.*;

public class CreatorResponseInjector implements ParameterResolver {
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
            .equals(CreatorResponse.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        CreatorResponse response = new CreatorResponse();
        response.network = new NetworkMeta();
        response.network.id = UUID.randomUUID().toString();
        return response;
    }
}
