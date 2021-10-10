package extensions.injectors;

import extensions.mocks.*;
import org.junit.jupiter.api.extension.*;
import util.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;
import wappsto.api.session.model.*;

import static util.Env.*;

public class AdminInjector implements ParameterResolver {

    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType().equals(Admin.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    )
        throws ParameterResolutionException
    {
        if (extensionContext.getTags().contains("unit")) {
            return new InMemoryAdmin();
        } else {
            try {
                return new RestAdmin(
                    new AdminCredentials(
                        env().get(Env.ADMIN_USERNAME),
                        env().get(Env.ADMIN_PASSWORD)
                    ),
                    env().get(API_ROOT)
                );
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to create admin session: " + e.getMessage()
                );
            }
        }

    }

}
