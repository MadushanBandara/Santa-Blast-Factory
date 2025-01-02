package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.map.Tile;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {

    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(3, 7);

    public static final TextureRegion ENTRANCE = SpriteSheet.BASIC_TILES.at(7, 3);

    public static final TextureRegion BAK_TILE = SpriteSheet.BASIC_TILES.at(9,1);

    public static final TextureRegion BREAK_TILE = SpriteSheet.BASIC_TILES.at(1,3);

    public static final TextureRegion UN_BREAK_TILE = SpriteSheet.HOUSE.at(1,1);

    public static final TextureRegion SPECIAL = SpriteSheet.TREE.at(4,7);

    public static final TextureRegion CHEST = SpriteSheet.BASIC_TILES.at(9,7);
    public static final TextureRegion INDESTRUCTIBLEWALLS = SpriteSheet.BASIC_TILES.at(1, 1);



    /**
     * Returns the texture associated with the given tile type.
     * @param tileType the type of the tile (INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, EMPTY, etc.)
     * @return the TextureRegion corresponding to the tile type
     */
    public static TextureRegion getTextureForTileType(int tileType) {
        switch (tileType) {
            case Tile.INDESTRUCTIBLE_WALL:
                return UN_BREAK_TILE;
            case Tile.DESTRUCTIBLE_WALL:
                return BREAK_TILE;
            case Tile.EMPTY:
                return BAK_TILE;
            case Tile.ENTRANCE:
                return ENTRANCE;
            case Tile.SPECIAL:
                return SPECIAL; // Replace with actual special tile texture
            case Tile.POWERUP:
                return CHEST; // Replace with actual power-up texture
            default:
                return BAK_TILE; // Default texture for unknown tile types
        }
    }



}
