package extensions.injectors;

import extensions.mocks.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.rest.wapps.*;
import wappsto.api.wapps.*;

import static util.Env.*;
import static util.Utils.*;

public class WappServiceInjector implements ParameterResolver {
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
            .equals(WappService.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        if (extensionContext.getTags().contains("unit")) {
            return new InMemoryWappService();
        } else {
            return new RestWappService(
                createNewUserSession(env().get(API_ROOT), createNewAdmin())
            );
        }
    }

}
