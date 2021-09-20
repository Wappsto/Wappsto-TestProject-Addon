package wappsto.rest.request;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

class Get extends Request {
    public Get(WebTarget service) {
        super(service);
    }
    
    public Response send() throws Exception {
        Response response = service
            .request()
            .get();
        
        return handle(response);
    }

    public Response send(String session) throws Exception {
        Response response = service
            .request()
            .header(SESSION_HEADER, session)
            .get();

        return handle(response);
    }
}
