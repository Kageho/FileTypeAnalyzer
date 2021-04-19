package analyzer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class KMPalgo extends Algorithm {
    KMPalgo(List<String> patterns, Path path) {
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
        boolean result = false;
        if (pattern.length() < text.length()) {
            int[] prefix = prefixFunc(pattern);
            int board = 0;
            for (int i = 0; i < text.length(); i++) {
                while (board > 0 && text.charAt(i) != pattern.charAt(board)) {
                    board = prefix[board - 1];
                }
                if (text.charAt(i) == pattern.charAt(board)) {
                    board++;
                }
                if (board == pattern.length()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private int[] prefixFunc(String pattern) {
        int[] prefix = new int[pattern.length()];
        int board;
        for (int i = 1; i < prefix.length; i++) {
            board = prefix[i - 1];
            while (board > 0 && pattern.charAt(i) != pattern.charAt(board)) {
                board = prefix[board - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(board)) {
                board++;
            }
            prefix[i] = board;
        }
        return prefix;
    }
}
