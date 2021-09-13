package wappsto.rest.wapps;

import wappsto.rest.wapps.model.InstallationRequest;
import wappsto.rest.wapps.model.WappsResponse;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;
import wappsto.rest.session.Session;

import javax.ws.rs.core.Response;
import java.util.Collection;

public class WappService {
    private Session session;

    public WappService(Session session) {
        this.session = session;
    }

    /**
     * Install a Wapp on the logged-in user
     * @param wapp
     * @throws Exception
     */
    public void install(Wapps wapp) throws Exception {
        InstallationRequest install = new InstallationRequest(
            wapp.id
        );

        new Request.Builder(session.service)
            .atEndPoint(API.INSTALLATION)
            .withBody(install)
            .post(session.id);
    }

    /**
     * Install a Wapp by name on the logged-in user
     * @param name
     * @throws Exception
     */
    public void install(String name) throws Exception {
        install(Wapps.from(name));
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
}
