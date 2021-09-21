package wappsto.iot.exceptions;

public class InvalidMessage extends Exception{
    public static final String MISSING_OPENING_BRACKET =
        "Message did not start with '{'";
    public static final String MISSING_CLOSING_BRACKET =
        "Message did not close with '{'";
    public InvalidMessage(String msg) {
        super(msg);
    }
}

