package wappsto.rest.request;

import wappsto.rest.exceptions.HttpException;
import wappsto.rest.exceptions.MissingField;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class PostRequest extends Request{
    public PostRequest(
        WebTarget service,
        String session,
        API endPoint,
        Object body
    ){
        super(service, session, endPoint, body);
    }

    public Response send() throws Exception {
        if (body == null) {
            throw new MissingField("Body in a post request cannot be null");
        }
        Response response;
        try{
            response= invoke()
                .post(Entity.json(body));
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return handleResponse(response);
    }
}
