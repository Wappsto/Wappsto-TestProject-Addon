package extensions;

import org.junit.jupiter.api.extension.*;
import util.*;
import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

import static util.Env.API_ROOT;
import static util.Env.env;

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
            return null;
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
