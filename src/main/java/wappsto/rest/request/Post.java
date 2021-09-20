package wappsto.rest.request;

import wappsto.rest.request.exceptions.MissingField;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

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
        Response response = service
            .request("application/json")
            .header(SESSION_HEADER, session)
            .post(body);

        return handle(response);
    }
}
