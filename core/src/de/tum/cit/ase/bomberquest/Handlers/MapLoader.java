package de.tum.cit.ase.bomberquest.Handlers;

import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.map.Tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created this class to load the map.
 * References:
            //https://www.geeksforgeeks.org/split-string-java-examples/
            //https://www.baeldung.com/reading-file-in-java
 #*/




public class MapLoader {

    private static World world;


    public static List<Tile> loadMap(World world, String filePath) {
        List<Tile> tiles = new ArrayList<>();
        Map<String, Integer> definedTiles = new HashMap<>();

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
                tiles.add(new Tile(world, x, y, type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tiles;
    }
}

