package wappsto.iot;

import wappsto.iot.exceptions.InvalidMessage;

public interface Callback {
    abstract void call(String message);
}
