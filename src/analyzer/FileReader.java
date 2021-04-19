package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {
    private String content;

    public boolean read(Path source) {
        try {
            content = new String(Files.readAllBytes(source));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getContent() {
        return content;
    }
}
