package wappsto.iot;

public interface Connection {
    void start(Callback messageCallback, Callback errorCallback) throws InterruptedException;
}
