package wappsto.api.rest.request;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

class Delete extends Request {
    public Delete(WebTarget service) {
        super(service);
    }

    public Response send() throws Exception {
        Response response = service
            .request()
            .delete();
        return handle(response);
    }

    public Response send(String session) throws Exception {
        Response response = service
            .request()
            .header(SESSION_HEADER, session)
            .delete();

        return handle(response);
    }
}
