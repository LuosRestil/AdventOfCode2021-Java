import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day12 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/inputs/Day12.txt"));
        List<String> input = new ArrayList<>();
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        scanner.close();

        Map<String, List<String>> caveMap = new HashMap<>();
        for (String line : input) {
            String[] splitLine = line.split("-");
            if (caveMap.containsKey(splitLine[0])) {
                caveMap.get(splitLine[0]).add(splitLine[1]);
            } else {
                caveMap.put(splitLine[0], new ArrayList<>());
                caveMap.get(splitLine[0]).add(splitLine[1]);
            }

            if (caveMap.containsKey(splitLine[1])) {
                caveMap.get(splitLine[1]).add(splitLine[0]);
            } else {
                caveMap.put(splitLine[1], new ArrayList<>());
                caveMap.get(splitLine[1]).add(splitLine[0]);
            }
        }

        List<List<String>> paths = explore(caveMap, "start", new ArrayList<>());

        int totalPaths = 0;
        for (List<String> path : paths) {
            if (path.get(path.size() - 1).equals("end")) {
                totalPaths++;
                System.out.println(path.toString());
            }
        }
        System.out.println("total paths: " + totalPaths);

    }

    public static List<List<String>> explore(Map<String, List<String>> caveMap, String location, List<String> pathSoFar) {
        List<String> pathSoFarCopy = new ArrayList<>(pathSoFar);
        pathSoFarCopy.add(location);
        // ...cave
        List<List<String>> paths = new ArrayList<>();
        paths.add(pathSoFarCopy);

        if (!location.equals("end")) {
            for (String cave : caveMap.get(location)) {
                if (!(isSmallCave(cave) && pathSoFarCopy.contains(cave))) {
                    paths.addAll(explore(caveMap, cave, pathSoFarCopy));
                }
            }
        }

        return paths;
    }

    public static boolean isSmallCave(String cave) {
        char firstChar = cave.charAt(0);
        return (int) firstChar >= 97 && (int) firstChar <= 122;
    }
}
