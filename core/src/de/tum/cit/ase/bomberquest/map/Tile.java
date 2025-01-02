package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Represents a single tile on the game map.
 */
public class Tile {

    public static final int INDESTRUCTIBLE_WALL = 0;
    public static final int DESTRUCTIBLE_WALL = 1;
    public static final int EMPTY = 9;
    public static final int ENTRANCE = 2;
    public static final int SPECIAL = 5;
    public static final int POWERUP = 6;

    private final int x;
    private final int y;
    private final int tileType;

    public Tile(int x, int y, int tileType) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
    }

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
     * @return A TextureRegion representing the tile's appearance.
     */
    public TextureRegion getCurrentAppearance() {
        return Textures.getTextureForTileType(tileType);
    }
}
