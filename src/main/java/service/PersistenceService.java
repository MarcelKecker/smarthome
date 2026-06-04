package service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PersistenceService {

    private ObjectMapper mapper = new ObjectMapper();

    public void save(
            SmartHomeProject project,
            File file) throws IOException {

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, project);
    }

    public SmartHomeProject load(
            File file) throws IOException {

        return mapper.readValue(
                file,
                SmartHomeProject.class
        );
    }
}