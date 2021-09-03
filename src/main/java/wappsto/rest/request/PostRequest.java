package wappsto.rest.request;

import wappsto.rest.exceptions.HttpException;
import wappsto.rest.exceptions.MissingField;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class PostRequest extends Request{
    public PostRequest(
        WebTarget service,
        Object body
    ){
        super(service, body);
    }

    public Response send() throws Exception {
        if (body == null) {
            throw new MissingField("Body in a post request cannot be null");
        }
        Response response = service
            .request()
            .post(Entity.json(body));

        return handle(response);
    }

    public Response send(String session) throws Exception {
        Response response = service
            .request("application/json")
            .header(SESSION_HEADER, session)
            .post(Entity.json(body));

        return handle(response);
    }
}
