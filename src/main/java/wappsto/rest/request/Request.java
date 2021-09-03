package wappsto.rest.request;

import wappsto.rest.exceptions.HttpException;
import wappsto.rest.exceptions.MissingField;

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

    public Request(WebTarget service) {
        this.service = service;
    }

    public Request(WebTarget service, String session, API endPoint) {
        this(service);
        this.session = session;
        this.endPoint = endPoint;
    }

    public Request(
        WebTarget service,
        String session,
        API endPoint,
        Object body
    ) {
        this(service, session, endPoint);
        this.body = body;
    }

    public Response post() throws Exception {
        Response response;
        try{
             response= invoke()
                .post(Entity.json(body));
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return handleResponse(response);
    }

    public Response get(String path) throws Exception {
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

    private Response handleResponse(Response response) throws Exception {
        switch (HttpResponse.from(response.getStatus())) {
            case OK:
            case CREATED:
                return response;
            case BAD_REQUEST:
            case FORBIDDEN:
            case UNAUTHORIZED:
            case NOT_FOUND:
                throw new HttpException(response.getStatusInfo().getReasonPhrase());
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

    public static class Builder {
        private WebTarget service;
        private String session;
        private API endPoint;
        private Object body;

        public Builder(WebTarget service) {
            this.service = service;
        }

        public Builder withSession(String session) {
            this.session = session;
            return this;
        }

        public Builder withBody(Object body) {
            this.body = body;
            return this;
        }

        public Builder atEndPoint(API endPoint) {
            this.endPoint = endPoint;
            return this;
        }

        public Response get(String path) throws Exception {
            return new Request(
                service,
                session,
                endPoint
            ).get(path);
        }

        public Response post() throws Exception {
            if (body == null) {
                throw new MissingField("Body in a post request cannot be null");
            }

            return new Request(
                service,
                session,
                endPoint,
                body
            ).post();
        }

        public Response delete(String path) throws Exception {
            return new Request(
                service,
                session,
                endPoint
            ) .delete(path);
        }
    }
}
