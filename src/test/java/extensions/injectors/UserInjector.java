package extensions.injectors;

import extensions.mocks.*;
import org.junit.jupiter.api.extension.*;
import wappsto.rest.session.*;
import wappsto.session.*;

import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.*;

public class UserInjector implements ParameterResolver {
    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType().equals(User.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        if (extensionContext.getTags().contains("unit")) {
            return new InMemoryUser(defaultUser(), "ignore");
        } else {
            RestAdmin admin = createNewAdmin();
            return createNewUserSession(env().get(API_ROOT), admin);
        }
    }
}
