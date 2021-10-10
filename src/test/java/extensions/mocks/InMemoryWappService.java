package extensions.mocks;

import wappsto.api.wapps.*;
import wappsto.api.wapps.model.*;

import java.util.*;

public class InMemoryWappService implements WappService {
    private LinkedList<Wapp> available;
    private LinkedList<String> installed;

    public InMemoryWappService() {
        available = new LinkedList<>();
        Wapp wapp = new Wapp();
        wapp.name = "wapp";
        available.add(wapp);
        installed = new LinkedList<>();
    }

    @Override
    public void install(String name) throws Exception {
        if (available.stream().anyMatch(wapp -> wapp.name.equals(name))) {
            installed.add(name);
        } else throw new Exception("Not found");
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
