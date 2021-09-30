package wappsto.rest.wapps;

import wappsto.rest.request.Request;
import wappsto.rest.request.*;
import wappsto.rest.session.*;
import wappsto.wapps.model.*;

import java.util.*;

/**
 * Manages Wapps on a user
 */
public class WappService implements wappsto.wapps.WappService {
    private final Session session;

    /**
     * Instantiate the Wapp service with a user session
     * @param session user session
     */
    public WappService(Session session) {
        this.session = session;
    }

    /**
     * Install a Wapp by name on the logged-in user
     * @param name Name of wapp
     * @throws Exception
     */
    @Override
    public void install(String name) throws Exception {
        String applicationId = fetchFromStore(name).application;
        InstallationRequest install = new InstallationRequest(applicationId);

        new Request.Builder(session.service)
            .atEndPoint(API.INSTALLATION)
            .withBody(install)
            .post(session.id);
    }


    /**
     * Fetch all Wapps installed on the user. Currently, not much useful info
     * from the response gets deserialized into POJO
     * @return
     * @throws Exception
     */
    @Override
    public Collection<String> fetchInstalled() throws Exception {
        return ((WappsResponse) new Request.Builder(session.service)
            .atEndPoint(API.INSTALLATION)
            .get(session.id, WappsResponse.class)).id;

    }

    /**
     * Fetches all Wapps from the store
     * @return Collection of all wapps
     * @throws Exception
     */
    @Override
    public Collection<Wapp> fetchAllFromStore() throws Exception {
        return ((MarketResponse) new Request.Builder(session.service)
            .atEndPoint(API.MARKET)
            .get(MarketResponse.class))
            .wapps;
    }

    private Wapp fetchFromStore(String name) throws Exception {
        return fetchAllFromStore()
            .stream()
            .filter(wapp -> wapp.name.equals(name))
            .findFirst()
            .orElseThrow(() -> new Exception("Wapp not found"));
    }
}
