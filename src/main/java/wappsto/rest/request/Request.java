package wappsto.rest.request;

import wappsto.rest.request.exceptions.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

/**
 * Base HTTP Request class
 */
public abstract class Request {
    public static final String SESSION_HEADER = "x-session";
    protected final WebTarget service;
    protected Entity body;

    /**
     * Request without JSON body
     * @param service
     */
    public Request(WebTarget service) {
        this.service = service;
    }

    /**
     * Request with JSON body
     * @param service
     * @param body JSON request body
     */
    public Request(
        WebTarget service,
        Entity body
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

    /**
     * HTTP Request Builder class
     */
    public static class Builder {
        private WebTarget service;
        private Entity body;


        /**
         * @param service
         */
        public Builder(WebTarget service) {
            this.service = service;
        }

        /**
         * Serialize a POJO into JSON
         * @param body
         * @return this
         */
        public Builder withBody(Object body) {
            this.body = Entity.json(body);
            return this;
        }

        /**
         * Append the given path to the requested URL
         * @param path  The relative path to the endpoint
         * @return this
         */
        public Builder atEndPoint(String path) {
            this.service = service.path(path);
            return this;
        }

        /**
         * Append the given path to the requested URL
         * @param path  The relative path to the endpoint
         * @return this
         */
        public Builder atEndPoint(API path) {
            return atEndPoint(path.toString());
        }

        /**
         * Send a GET request to the given endpoint
         * @return JSON response body
         * @throws Exception
         */
        public Response get() throws Exception {
            return new Get(
                service
            ).send();
        }

        /**
         * Send a GET request with a session header to the given endpoint
         * @param session
         * @return JSON response body
         * @throws Exception
         */
        public Response get(String session) throws Exception {
            return new Get(
                service
            ).send(session);
        }

        /**
         * Send a POST request to the given endpoint.
         * @return JSON response body
         * @throws Exception
         */
        public Response post() throws Exception {
            return new Post(
                service,
                body
            ).send();
        }

        /**
         * Send a POST request with a session header to the given endpoint
         * @param session
         * @return JSON response body
         * @throws Exception
         */
        public Response post(String session) throws Exception {
            return new Post(
                service,
                body
            ).send(session);
        }

        /**
         * Send a DELETE request to the given endpoint
         * @return JSON response body
         * @throws Exception
         */
        public Response delete() throws Exception {
            return new Delete(
                service
            ).send();
        }

        /**
         * Send a DELETE request with a session header to the given endpoint
         * @param session
         * @return JSON response body
         * @throws Exception
         */
        public Response delete(String session) throws Exception {
            return new Delete(
                service
            ).send(session);
        }

        public Response patch(String session) throws Exception {
            return new Patch(service, body).send(session);
        }
    }
}
