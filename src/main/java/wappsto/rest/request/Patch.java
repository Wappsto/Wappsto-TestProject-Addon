package wappsto.rest.request;

import com.fasterxml.jackson.databind.*;
import org.glassfish.jersey.client.*;
import wappsto.rest.request.exceptions.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.*;

public class Patch extends Request{
    public Patch(WebTarget service, Entity body){
        super(service, body);
    }

    public Response send() throws Exception {
        if (body == null) {
            throw new MissingField("Body in a post request cannot be null");
        }

        Invocation.Builder builder = service
            .request()
            .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
            .header(CONTENT_TYPE, APPLICATION_JSON);
        Response response = builder.method("PATCH", body);

        return handle(response);
    }

    public Response send(String session) throws Exception {
        if (body == null) {
            throw new MissingField("Body in a post request cannot be null");
        }

        System.out.println(new ObjectMapper().writeValueAsString(body));

        Invocation.Builder builder = service
            .request("application/json")
            .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
            .header(SESSION_HEADER, session);
        Response response = builder.method("PATCH", body);

        return handle(response);
    }
}
