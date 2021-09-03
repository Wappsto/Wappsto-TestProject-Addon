package wappsto.rest.request;

import wappsto.rest.exceptions.HttpException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public abstract class Request {
    public static final String SESSION_HEADER = "x-session";
    protected WebTarget service;
    protected Object body;

    public Request(WebTarget service) {
        this.service = service;
    }

    public Request(
        WebTarget service,
        Object body
    ) {
        this(service);
        this.body = body;
    }

    public abstract Response send() throws Exception;
    public abstract Response send(String session) throws Exception;

    protected Response handle(Response response) throws Exception {
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

    public static class Builder {
        private WebTarget service;
        private Object body;

        public Builder(WebTarget service) {
            this.service = service;
        }

        public Builder withBody(Object body) {
            this.body = body;
            return this;
        }

        public Builder addPath(String path) {
            this.service = service.path(path);
            return this;
        }

        public Response get() throws Exception {
            return new GetRequest(
                service
            ).send();
        }

        public Response get(String session) throws Exception {
            return new GetRequest(
                service
            ).send(session);
        }

        public Response post() throws Exception {
            return new PostRequest(
                service,
                body
            ).send();
        }

        public Response post(String session) throws Exception {
            return new PostRequest(
                service,
                body
            ).send(session);
        }

        public Response delete() throws Exception {
            return new DeleteRequest(
                service
            ).send();
        }

        public Response delete(String session) throws Exception {
            return new DeleteRequest(
                service
            ).send(session);
        }
    }
}
