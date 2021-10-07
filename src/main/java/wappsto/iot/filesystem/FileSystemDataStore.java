package wappsto.iot.filesystem;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.*;

import java.io.*;
import java.util.*;

public class FileSystemDataStore implements DataStore {
    private final String path;
    private final String fileExtension;

    public FileSystemDataStore(String path, String fileExtension) {
        this.path = path;
        new File(path).mkdirs();
        this.fileExtension = fileExtension;
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
    public Object load(String identifier, Class<?> type) throws Exception {
        File file = new File(
            pathToFile(identifier)
        );
        Scanner reader = new Scanner(file);
        String data = "";
        while (reader.hasNextLine()) {
            data.concat(reader.nextLine());
        }
        return new ObjectMapper().readValue(data, type);
    }

    private String pathToFile(String identifier) {
        return path.concat(identifier).concat(".").concat(fileExtension);
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
