package wappsto.api.rest.request.exceptions;

public class MissingField extends Exception {
    public MissingField(String field) {
        super(String.format("Missing field: required field '%s' is missing", field));
    }
}
