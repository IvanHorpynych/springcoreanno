package main.appannotation;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
@Component
public class FileEventLogger implements EventLogger {

    @Value("${filePath}")
    private String filePath;
    private File file;
    @Value("${encoding}")
    private String encoding;

    @PostConstruct
    private void init() throws IOException {
        this.file = new File(filePath);
        if (file.createNewFile() && !file.canWrite())
            throw new IOException("Not have access to log file!");
    }

    public void logEvent(Event event) {
        try {
            FileUtils.writeStringToFile(new File(filePath), event.toString(), encoding, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
