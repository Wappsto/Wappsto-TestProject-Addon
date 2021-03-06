package wappsto.api.rest.request;

import wappsto.api.rest.request.exceptions.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

class Post extends Request{

    public Post(WebTarget service, Entity body){
        super(service, body);
    }

    public Response send() throws Exception {
        if (body == null) {
            throw new MissingField("Body in a post request cannot be null");
        }
        Response response = service
            .request()
            .post(body);

        return handle(response);
    }

    public Response send(String session) throws Exception {
        if (body == null) {
            throw new MissingField("Body in a post request cannot be null");
        }

        Response response = service
            .request("application/json")
            .header(SESSION_HEADER, session)
            .post(body);

        return handle(response);
    }
}
