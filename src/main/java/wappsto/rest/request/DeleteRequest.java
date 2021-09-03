package wappsto.rest.request;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class DeleteRequest extends Request {
    public DeleteRequest(WebTarget service) {
        super(
            service
        );
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
