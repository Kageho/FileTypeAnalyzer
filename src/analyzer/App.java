package analyzer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public void run(String[] args) {
        List<Path> paths = getPathToFiles(args[0]);
        Patterns pattern = new Patterns(args[1]);
        String algorithm = args[2];
        if (!pattern.readFile()) {
            System.out.println("smth is went wrong, try again later");
            return;
        }
        var result = getResults(algorithm, paths, pattern);
        printResults(result, pattern);
    }

    private void printResults(List<Future<Map<String, String>>> result, Patterns patterns) {
        for (var i : result) {
            try {
                Map<String, String> answer = i.get();
                for (var j : answer.entrySet()) {
                    System.out.printf("%s: %s\n", j.getKey(), patterns.contains(j.getValue())
                            ? patterns.getType(j.getValue()) : j.getValue());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Future<Map<String, String>>> getResults(String algoType, List<Path> paths, Patterns patterns) {
        ExecutorService executors = Executors.newFixedThreadPool(8);
        List<Future<Map<String, String>>> result = new ArrayList<>();
        switch (algoType) {
            case "KMP":
                for (var i : paths) {
                    KMPalgo kmPalgo = new KMPalgo(patterns.getPatterns(), i);
                    result.add(executors.submit(kmPalgo.getResult()));
                }
                break;
            case "naive":
                for (var i : paths) {
                    NaiveAlgo naiveAlgo = new NaiveAlgo(patterns.getPatterns(), i);
                    result.add(executors.submit(naiveAlgo.getResult()));
                }
                break;
            case "Rabin-Karp":
                for (var i : paths) {
                    RabinKarpAlgo rabinKarpAlgo = new RabinKarpAlgo(patterns.getPatterns(), i);
                    result.add(executors.submit(rabinKarpAlgo.getResult()));
                }
                break;
            default:
                System.out.println("There is no such option");

        }
        executors.shutdown();
        return result;
    }

    private List<Path> getPathToFiles(String dir) {
        List<Path> result = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Path.of(dir))) {
            result = walk.filter(f -> !Files.isDirectory(f))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
