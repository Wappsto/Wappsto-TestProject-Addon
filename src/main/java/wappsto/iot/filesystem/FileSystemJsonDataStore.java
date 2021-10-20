package wappsto.iot.filesystem;

import com.fasterxml.jackson.databind.*;
import org.aspectj.util.*;
import wappsto.iot.*;

import java.io.*;

/**
 * Stores and loads network instances with certs and schemas on the file system
 */
public class FileSystemJsonDataStore implements DataStore {
    private final String path;
    private static final String FILE_EXTENSION = "json";

    /**
     * Instantiates the data store with the default path
     */
    public FileSystemJsonDataStore() {
        this("./saved_instance/");
    }

    /**
     * Instantiates the data store with a given path
     * @param path relative path
     */
    public FileSystemJsonDataStore(String path) {
        this.path = path;
        new File(path).mkdirs();
    }

    /**
     * Saves an instance in data store.
     * @param identifier filename
     * @param data saved data
     */
    @Override
    public void save(String identifier, Object data) {
        File file = new File(
            pathToFile(identifier)
        );
        try {
            if (!file.createNewFile()) {
                clear(file);
            };
            PrintWriter writer = new PrintWriter(file);
            writer.write(new ObjectMapper().writeValueAsString(data));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

    /**
     * Loads an object from a JSON file on the file system
     * @param identifier filename
     * @param T Object type to deserialize the json into
     * @return the loaded object
     * @throws Exception
     */
    @Override
    public Object load(String identifier, Class<?> T) throws Exception {
        File file = new File(
            pathToFile(identifier)
        );
        String data = FileUtil.readAsString(file);
        return new ObjectMapper().readValue(data, T);
    }

    public void delete(String identifier) {
        new File(pathToFile(identifier)).delete();
    }

    private String pathToFile(String identifier) {
        return path.concat(identifier).concat(".").concat(FILE_EXTENSION);
    }

    private void clear(File file) throws IOException {
        FileWriter fwOb = new FileWriter(file, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }
}
