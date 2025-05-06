package game.map;

import java.io.*;
import java.util.*;
import game.model.TileType;

public class MapManager {
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