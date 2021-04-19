package analyzer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class RabinKarpAlgo extends Algorithm {
    private static final long m = 173_961_102_589_771L;
    private static final int a = 117;

    RabinKarpAlgo(List<String> patterns, Path path) {
        super(patterns, path);
    }

    @Override
    Callable<Map<String, String>> getResult() {
        return new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                Map<String, String> result = new HashMap<>();
                for (var pattern : patterns) {
                    if (isFound(pattern)) {
                        result.put(fileName, pattern);
                        break;
                    }
                }
                return result.isEmpty() ? Map.of(fileName, "Unknown file type") : result;
            }
        };
    }


    private boolean isFound(String pattern) {
        if (pattern.length() > text.length()) {
            return false;
        }
        long patternHash = 0;
        long currSubStrHash = 0;
        long pow = 1;
        for (int i = 0; i < pattern.length(); i++) {
            patternHash += (pattern.charAt(i)) * pow;
            patternHash %= m;

            currSubStrHash += (text.charAt(text.length() - pattern.length() + i)) * pow;
            currSubStrHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        boolean contain = false;
        for (int i = text.length(); i >= pattern.length(); i--) {
            if (patternHash == currSubStrHash) {
                boolean patternIsFound = true;

                for (int j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                        patternIsFound = false;
                        break;
                    }
                }

                if (patternIsFound) {
                    contain = true;
                    break;
                }
            }
            if (i > pattern.length()) {
                currSubStrHash = (currSubStrHash - (text.charAt(i - 1)) * pow % m + m) * a % m;
                currSubStrHash = (currSubStrHash + (text.charAt(i - pattern.length() - 1))) % m;
            }
        }
        return contain;
    }
}
