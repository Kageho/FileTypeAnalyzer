package analyzer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Algorithm {
    List<String> patterns;
    final String text;
    final String fileName;

    Algorithm(List<String> patterns, Path path) {
        this.patterns = patterns;
        FileReader fileReader = new FileReader();
        fileReader.read(path);
        text = fileReader.getContent();
        fileName = path.getFileName().toString();
    }

    abstract Callable<Map<String, String>> getResult();
}
