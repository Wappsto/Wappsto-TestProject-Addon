package extensions.injectors;

import extensions.mocks.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.*;

public class DataStoreInjector implements ParameterResolver {
    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType().equals(DataStore.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return new InMemoryDataStore();
    }
}
