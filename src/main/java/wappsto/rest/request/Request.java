package wappsto.rest.request;

import wappsto.rest.exceptions.HttpException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public abstract class Request {
    public static final String SESSION_HEADER = "x-session";
    protected WebTarget service;
    protected String session;
    protected API endPoint;
    protected Object body;

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

    protected Response handleResponse(Response response) throws Exception {
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

    protected Invocation.Builder invoke() {
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
            return new GetRequest(
                service,
                session,
                endPoint
            ).send(path);
        }

        public Response post() throws Exception {
            return new PostRequest(
                service,
                session,
                endPoint,
                body
            ).send();
        }

        public Response delete(String path) throws Exception {
            return new DeleteRequest(
                service,
                session,
                endPoint
            ) .send(path);
        }
    }
}
