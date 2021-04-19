package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Patterns {
    private String path;
    private List<String> patterns;
    private Map<String, String> patToType;

    Patterns(String path) {
        this.path = path;
        patterns = new ArrayList<>();
        patToType = new HashMap<>();
    }

    boolean readFile() {
        boolean result = true;
        try (Scanner scanner = new Scanner(new File(path))) {
            String content;
            while (scanner.hasNextLine()) {
                content = scanner.nextLine();
                String[] args = content.split(";");
                patterns.add(args[1].substring(1, args[1].length() - 1));
                patToType.put(patterns.get(patterns.size() - 1), args[2].substring(1, args[2].length() - 1));
            }
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            result = false;
        }
        Collections.reverse(patterns);
        return result;
    }

    List<String> getPatterns() {
        return patterns;
    }

    boolean contains(String key) {
        return patToType.containsKey(key);
    }

    String getType(String key) {
        return patToType.getOrDefault(key, "No such key");
    }
}
