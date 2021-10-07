package wappsto.iot.filesystem;

import com.fasterxml.jackson.databind.*;
import org.aspectj.util.*;
import wappsto.iot.*;

import java.io.*;

public class FileSystemJsonDataStore implements DataStore {
    private final String path;
    private static final String FILE_EXTENSION = "json";

    public FileSystemJsonDataStore(String path) {
        this.path = path;
        new File(path).mkdirs();
    }

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

    @Override
    public Object load(String identifier, Class<?> T) throws Exception {
        File file = new File(
            pathToFile(identifier)
        );
        String data = FileUtil.readAsString(file);
        return new ObjectMapper().readValue(data, T);
    }

    private String pathToFile(String identifier) {
        return path.concat(identifier).concat(".").concat(FILE_EXTENSION);
    }

    public void delete(String identifier) {
        new File(pathToFile(identifier)).delete();
    }

    private void clear(File file) throws IOException {
        FileWriter fwOb = new FileWriter(file, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }
}
