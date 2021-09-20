package wappsto.rest.wapps;

import wappsto.rest.request.Request;
import wappsto.rest.request.*;
import wappsto.rest.session.*;
import wappsto.rest.wapps.model.*;

import javax.ws.rs.core.*;
import java.util.*;

public class WappService {
    private Session session;

    public WappService(Session session) {
        this.session = session;
    }

    /**
     * Install a Wapp by name on the logged-in user
     * @param name Name of wapp
     * @throws Exception
     */
    public void install(String name) throws Exception {
        String applicationId = fetchFromStore(name)
            .application;

        InstallationRequest install = new InstallationRequest(
            applicationId
        );

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
    public Collection<String> fetchInstalled() throws Exception {
        Response response = new Request.Builder(session.service)
            .atEndPoint(API.INSTALLATION)
            .get(session.id);

        return response.readEntity(WappsResponse.class).id;
    }

    public Collection<Wapp> fetchAllFromStore() throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.MARKET)
            .get()
            .readEntity(MarketResponse.class)
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
