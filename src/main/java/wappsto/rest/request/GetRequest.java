package wappsto.rest.request;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class GetRequest extends Request {
    public GetRequest(WebTarget service, String session, API endPoint) {
        super(service, session, endPoint);
    }

    public Response send(String path) throws Exception {
        Response response = service
            .path(endPoint.toString())
            .path(path)
            .request()
            .header(SESSION_HEADER, session)
            .get();

        return handleResponse(response);
    }
}
