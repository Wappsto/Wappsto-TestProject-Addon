package wappsto.iot;

public interface Connection {
    void setIncomingCallback(Callback callback) throws InterruptedException;
}
