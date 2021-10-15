package extensions.mocks;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import wappsto.iot.*;

import java.util.*;

public class InMemoryDataStore implements DataStore {
    HashMap<String, String> data;

    public InMemoryDataStore() {
        data = new HashMap<>();
    }

    @Override
    public void save(String identifier, Object data) {
        try {
            this.data.put(
                identifier,
                new ObjectMapper().writeValueAsString(data)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                "Failed to deserialize JSON: " + e.getMessage()
            );
        }
    }

    @Override
    public Object load(String identifier, Class<?> type) throws Exception {
        try {
            return new ObjectMapper().readValue(data.get(identifier), type);
        } catch (Exception e) {
            Exception error = new Exception(
                "Failed to load network from data store" + e.getMessage()
            );
            throw error;
        }
    }
}
