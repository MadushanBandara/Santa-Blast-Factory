package de.tum.cit.ase.bomberquest.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static List<Tile> loadMap(String filePath) {
        List<Tile> tiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {

                    continue;
                }
                String[] parts = line.split("=");
                String[] coordinates = parts[0].split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int type = Integer.parseInt(parts[1]);
                tiles.add(new Tile(x, y, type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tiles;
    }
}

