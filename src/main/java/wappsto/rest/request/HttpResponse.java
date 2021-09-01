package wappsto.rest.request;

public enum HttpResponse {
    OK(200),
    CREATED(201),
    FORBIDDEN(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    UNKNOWN(0);

    private final int value;

    HttpResponse(int value) {
        this.value = value;
    }

    public static HttpResponse from(int responseCode) {
        for (HttpResponse response : values()) {
            if (response.value == responseCode) {
                return response;
            }
        }
        return UNKNOWN;
    }
}
