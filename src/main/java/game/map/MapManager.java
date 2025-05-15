package game.map;

import java.io.*;
import java.util.*;

public class MapManager {
    /**
     * Loads a map from a file and converts it into a 2D character array.
     * This method reads the file line by line, extracts the map data enclosed
     * within the "map" section, and processes it into a 2D array of characters.
     * Any unnecessary characters such as quotes and commas are removed during processing.
     *
     * @param filename The name of the file containing the map data.
     * @return A 2D character array representing the loaded map.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static char[][] loadMap(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/maps/" + filename))) {
            String line;
            boolean insideMap = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"map\"")) {
                    insideMap = true;
                    continue;
                }
                if (insideMap) {
                    if (line.startsWith("]")) break;
                    line = line.replaceAll("[\",]", "").trim();
                    if (!line.isEmpty()) {
                        lines.add(line);
                    }
                }
            }
        }

        char[][] map = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }
        return map;
    }

    /**
     * Generates a new map with a single forbidden tile ('Z').
     * This method clones the existing map, removes any existing forbidden tiles ('Z'),
     * and randomly places a new forbidden tile on an empty space (' ').
     *
     * @param existingMap The original 2D character array representing the map.
     * @return A new 2D character array with a single forbidden tile ('Z') placed randomly.
     */
    public static char[][] generateForbiddenOnly(char[][] existingMap) {
        char[][] newMap = Arrays.stream(existingMap)
                .map(char[]::clone)
                .toArray(char[][]::new);

        // Tady ostranuju to staré Z
        for (int i = 0; i < newMap.length; i++) {
            for (int j = 0; j < newMap[i].length; j++) {
                if (newMap[i][j] == 'Z') {
                    newMap[i][j] = ' ';
                }
            }
        }

        List<int[]> emptyTiles = new ArrayList<>();
        for (int i = 0; i < newMap.length; i++) {
            for (int j = 0; j < newMap[i].length; j++) {
                if (newMap[i][j] == ' ') {
                    emptyTiles.add(new int[]{i, j});
                }
            }
        }

        if (!emptyTiles.isEmpty()) {
            int[] chosen = emptyTiles.get(new Random().nextInt(emptyTiles.size()));
            newMap[chosen[0]][chosen[1]] = 'Z';
        }

        return newMap;
    }

    /**
     * Generates a new map with a single forbidden tile ('Z').
     * This method creates a copy of the given base map, identifies all empty tiles (' '),
     * and randomly places a forbidden tile ('Z') on one of the empty tiles.
     *
     * @param baseMap The original 2D character array representing the base map.
     * @return A new 2D character array with a single forbidden tile ('Z') placed randomly.
     */
    public static char[][] generateForbiddenTile(char[][] baseMap) {
        char[][] newMap = Arrays.stream(baseMap)
                .map(char[]::clone)
                .toArray(char[][]::new);

        List<int[]> emptyTiles = new ArrayList<>();
        for (int i = 0; i < newMap.length; i++) {
            for (int j = 0; j < newMap[i].length; j++) {
                if (newMap[i][j] == ' ') {
                    emptyTiles.add(new int[]{i, j});
                }
            }
        }

        if (!emptyTiles.isEmpty()) {
            int[] chosen = emptyTiles.get(new Random().nextInt(emptyTiles.size()));
            newMap[chosen[0]][chosen[1]] = 'Z';
        }

        return newMap;
    }
}