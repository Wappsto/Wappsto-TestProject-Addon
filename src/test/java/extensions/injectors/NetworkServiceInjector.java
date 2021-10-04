package extensions.injectors;

import extensions.mocks.*;
import org.junit.jupiter.api.extension.*;
import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

import static util.Env.*;
import static util.Utils.createNewAdmin;
import static util.Utils.createNewUserSession;

public class NetworkServiceInjector implements ParameterResolver {

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
            .equals(NetworkService.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        if (extensionContext.getTags().contains("unit")) {
            return new InMemoryNetworkService();
        } else {
            RestAdmin admin = createNewAdmin();
            RestUser session = createNewUserSession(
                env().get(API_ROOT),
                admin
            );
            return new RestNetworkService(session);

        }
    }

}
