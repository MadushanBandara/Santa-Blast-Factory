package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class Tile {
    public static final int INDESTRUCTIBLE_WALL = 0;
    public static final int DESTRUCTIBLE_WALL = 1;
    public static final int EMPTY = 9;
    public static final int ENTRANCE = 2;
    public static final int SPECIAL = 5;
    public static final int POWERUP = 6;

    private int x, y, tileType;

    public Tile(int x, int y, int tileType) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
    }

    // Getters for x, y, and tileType
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTileType() {
        return tileType;
    }


    /**
     * Returns the appearance of the tile based on its type.
     *
     * @return A string describing the appearance of the tile.
     */
}