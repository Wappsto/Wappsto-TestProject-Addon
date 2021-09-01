package wappsto.rest.request;

import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.exceptions.MissingField;
import wappsto.rest.exceptions.NotFound;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class Request {
    public static final String SESSION_HEADER = "x-session";
    private WebTarget service;
    private String session;
    private API endPoint;
    private Object body;
    private Object entity;

    public Request(WebTarget service) {
        this.service = service;
    }

    public Request(WebTarget service, String session, API endPoint, Object body) {
        this(service);
        this.session = session;
        this.endPoint = endPoint;
        this.body = body;
    }

    public Request withSession(String session) {
        return new Request(service, session, endPoint, body);
    }

    public Request withBody(Object body) {
        return new Request(service, session, endPoint, body);
    }

    public Request atEndPoint(API endPoint) {
        return new Request(service, session, endPoint, body);
    }


    public Response post() throws Exception {
        if (body == null) {
            throw new MissingField("body");
        }

        Response response = invoke()
            .post(Entity.json(body));

        return handleResponse(response);
    }

    public Response get(String path) throws Forbidden, NotFound {
        Response response = service
            .path(endPoint.toString())
            .path(path)
            .request()
            .header(SESSION_HEADER, session)
            .get();

        return handleResponse(response);
    }

    public Response delete(String path) throws Exception {
        Response response = service
            .path(endPoint.toString())
            .path(path)
            .request()
            .header(SESSION_HEADER, session)
            .delete();
        return handleResponse(response);
    }

    private Response handleResponse(Response response) throws Forbidden, NotFound {
        switch (HttpResponse.from(response.getStatus())) {
            case OK:
            case CREATED:
                return response;
            case FORBIDDEN:
            case UNAUTHORIZED:
                throw new Forbidden(response.getStatusInfo().getReasonPhrase());
            case NOT_FOUND:
                throw new NotFound(response.getStatusInfo().getReasonPhrase());
            default:
                throw new IllegalStateException("Unexpected value");
        }
    }

    private Invocation.Builder invoke() {
        Invocation.Builder invocation = service.path(endPoint.toString())
            .request("application/json");

        if (session != null) {
            invocation = invocation.header(SESSION_HEADER, session);
        }
        return invocation;
    }
}
