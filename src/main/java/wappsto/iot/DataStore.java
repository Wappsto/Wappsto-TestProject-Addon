package wappsto.iot;

public interface DataStore {
    void save(String identifier, Object data);

    Object load(String identifier, Class<?> type) throws Exception;
}
