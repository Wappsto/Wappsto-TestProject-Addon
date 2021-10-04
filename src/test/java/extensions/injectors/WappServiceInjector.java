package extensions.injectors;

import extensions.mocks.*;
import org.junit.jupiter.api.extension.*;
import wappsto.rest.wapps.*;
import wappsto.wapps.*;

import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.createNewAdmin;
import static util.Utils.createNewUserSession;

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
