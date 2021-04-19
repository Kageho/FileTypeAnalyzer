package analyzer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class NaiveAlgo extends Algorithm {
    NaiveAlgo(List<String> patterns, Path path) {
        super(patterns, path);
    }

    @Override
    Callable<Map<String, String>> getResult() {
        return new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                for (var pattern : patterns) {
                    if (pattern.length() <= text.length()) {
                        for (int j = 0; j < text.length() - pattern.length() + 1; j++) {
                            boolean patternIsFound = true;
                            for (int i = 0; i < pattern.length(); i++) {
                                if (text.charAt(i + j) != pattern.charAt(i)) {
                                    patternIsFound = false;
                                    break;
                                }
                            }
                            if (patternIsFound) {
                                return Map.of(fileName, pattern);
                            }
                        }
                    }
                }
                return Map.of(fileName, "Unknown file type");
            }
        };
    }
}
