package wappsto.rest.wapps;

import wappsto.rest.exceptions.WappNotFound;
import wappsto.rest.wapps.model.InstallationRequest;
import wappsto.rest.wapps.model.MarketResponse;
import wappsto.rest.wapps.model.Wapp;
import wappsto.rest.wapps.model.WappsResponse;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;
import wappsto.rest.session.Session;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.NoSuchElementException;

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

    private Wapp fetchFromStore(String name) throws Exception {
        try {
            return fetchAllFromStore()
                .stream()
                .filter(wapp -> wapp.name.equals(name))
                .findFirst()
                .get();
        } catch (NoSuchElementException e) {
            throw new WappNotFound("Wapp not found");
        }
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
}
