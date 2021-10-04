package extensions;

import org.junit.jupiter.api.extension.*;
import wappsto.rest.wapps.*;
import wappsto.wapps.*;
import wappsto.wapps.model.*;

import java.util.*;

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

    private static class InMemoryWappService implements WappService {
        private LinkedList<Wapp> available;
        private LinkedList<String> installed;

        public InMemoryWappService() {
            available = new LinkedList<>();
            Wapp wapp = new Wapp();
            wapp.name = "available";
            available.add(wapp);
            installed = new LinkedList<>();
        }

        @Override
        public void install(String name) throws Exception {
            if (available.contains(name)) installed.add(name);
            else throw new Exception("Not found");
        }

        @Override
        public Collection<String> fetchInstalled() throws Exception {
            return installed;
        }

        @Override
        public Collection<Wapp> fetchAllFromStore() throws Exception {
            return available;
        }
    }
}
