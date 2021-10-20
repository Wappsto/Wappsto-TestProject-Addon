package wappsto.api.rest.request;

import wappsto.api.rest.request.exceptions.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

/**
 * Base HTTP Request class
 */
public abstract class Request {
    public static final String SESSION_HEADER = "x-session";
    protected final WebTarget service;
    protected Entity<?> body;

    /**
     * Request without JSON body
     * @param service target url
     */
    public Request(WebTarget service) {
        this.service = service;
    }

    /**
     * Request with JSON body
     * @param service
     * @param body JSON request body
     */
    public Request(WebTarget service, Entity<?> body) {
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
            case PAYMENT_REQUIRED:
            case UNAUTHORIZED:
            case NOT_FOUND:
                throw new HttpException(
                    response.getStatusInfo().getReasonPhrase()
                );
            default:
                throw new IllegalStateException(
                    "Unexpected response: "
                        + response.getStatusInfo().getReasonPhrase()
                );
        }
    }

    /**
     * HTTP Request Builder class
     */
    public static class Builder {
        private WebTarget service;
        private Entity<?> body;


        /**
         * @param service target url
         */
        public Builder(WebTarget service) {
            this.service = service;
        }

        /**
         * Serialize a POJO into JSON
         * @param body JSON request body
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
         * @throws Exception
         */
        public void get() throws Exception {
            new Get(service).send();
        }

        /**
         * Send a GET request to the given endpoint
         * @return JSON response body
         * @throws Exception
         */
        public Object get(Class<?> T) throws Exception {
            return new Get(service).send().readEntity(T);
        }

        /**
         * Send a GET request with a session header to the given endpoint
         * @param session
         * @param T Desired return type
         * @return JSON response body
         * @throws Exception
         */
        public Object get(String session, Class<?> T) throws Exception {
            return new Get(service).send(session).readEntity(T);
        }

        /**
         * Send a POST request to the given endpoint
         * @throws Exception
         */
        public void post() throws Exception {
            new Post(service, body).send();
        }

        /**
         * Send a POST request to the given endpoint
         * @param session session header
         * @throws Exception
         */
        public void post(String session) throws Exception {
            new Post(service, body).send(session);
        }

        /**
         * Send a POST request to the given endpoint.
         * @param T Desired return type
         * @return JSON response body
         * @throws Exception
         */
        public Object post(Class<?> T) throws Exception {
            return new Post(service, body).send().readEntity(T);
        }

        /**
         * Send a POST request with a session header to the given endpoint
         * @param session
         * @return JSON response body
         * @throws Exception
         */
        public Object post(String session, Class<?> T) throws Exception {
            return new Post(service, body).send(session).readEntity(T);
        }

        /**
         * Send a DELETE request to the given endpoint
         * @throws Exception
         */
        public void delete() throws Exception {
            new Delete(service).send();
        }

        /**
         * Send a DELETE request to the given endpoint
         * @param session session header
         * @throws Exception
         */
        public void delete(String session) throws Exception {
            new Delete(service).send(session);
        }

        /**
         * Send a DELETE request to the given endpoint
         * @param T desired return type
         * @return JSON response body
         * @throws Exception
         */
        public Object delete(Class<?> T) throws Exception {
            return new Delete(service).send().readEntity(T);
        }

        /**
         * Send a DELETE request with a session header to the given endpoint
         * @param session
         * @param T desired return type
         * @return JSON response body
         * @throws Exception
         */
        public Object delete(String session, Class<?> T) throws Exception {
            return new Delete(service).send(session).readEntity(T);
        }

        /**
         * Send a PATCH request to the desired endpoint
         * @param session session header
         * @throws Exception
         */
        public void patch(String session) throws Exception {
            new Patch(service, body).send(session);
        }

        /**
         * Send a PATCh request to the desired enpoint
         * @param session session header
         * @param T desired return type
         * @return JSON response body
         * @throws Exception
         */
        public Object patch(String session, Class<?> T) throws Exception {
            return new Patch(service, body).send(session).readEntity(T);
        }
    }
}
