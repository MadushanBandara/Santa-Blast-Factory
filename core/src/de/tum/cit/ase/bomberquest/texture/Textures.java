package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.map.Tile;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {

    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(2, 4);
    /*
    public static final int INDESTRUCTIBLE_WALL = 0;
    public static final int DESTRUCTIBLE_WALL = 1;
    public static final int EMPTY = 9;
    public static final int ENTRANCE = 2;
    public static final int SPECIAL = 5;
    public static final int POWERUP = 6;
    */

    public static final TextureRegion ENTRANCE = SpriteSheet.BASIC_TILES.at(4, 8);
    public static final TextureRegion EXIT = SpriteSheet.ORIGINALBOMBERMAN.at(4, 12);
    public static final TextureRegion BAK_TILE = SpriteSheet.BASIC_TILES.at(2,4);
    public static final TextureRegion DEFAULT = SpriteSheet.BASIC_TILES.at(2,4);
    public static final TextureRegion UN_BREAK_TILE = SpriteSheet.WALLS.at(4,4);

    public static final TextureRegion BREAK_TILE= SpriteSheet.WALLS.at(4,5);

    public static final TextureRegion SPECIAL = SpriteSheet.TREE.at(4,7);

    public static final TextureRegion CHEST = SpriteSheet.BASIC_TILES.at(5,5);
    public static final TextureRegion OPENEDCHEST = SpriteSheet.BASIC_TILES.at(5,4);
    public static final TextureRegion SANTA = SpriteSheet.SANTA.at(5,1);
    public static final TextureRegion SNOWMAN=SpriteSheet.SNOWMAN.at(1,1);
    //public static final TextureRegion INDESTRUCTIBLEWALLS = SpriteSheet.WALLS.at(4, 5);

    //powerups
    public static final TextureRegion LIFE = SpriteSheet.ORIGINALBOMBERMAN.at(15, 5);
    public static final TextureRegion BLASTRADIUSPLUS= SpriteSheet.ORIGINALBOMBERMAN.at(15, 7);
    public static final TextureRegion KILLENEMY= SpriteSheet.ORIGINALBOMBERMAN.at(15, 6);
    public static final TextureRegion EXTRABOMBS= SpriteSheet.ORIGINALBOMBERMAN.at(15, 3);

    //powerdown
    public static final TextureRegion MOREENEMIES= SpriteSheet.ORIGINALBOMBERMAN.at(15, 2);
    public static final TextureRegion LESSBOMBS=SpriteSheet.ORIGINALBOMBERMAN.at(15, 3);





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
                return SNOWMAN; // Replace with actual power-up texture
            default:
                return DEFAULT; // Default texture for unknown tile types
        }
    }

    public static List<TextureRegion> randomSurprise = new ArrayList<>(List.of(
            LIFE, BLASTRADIUSPLUS, KILLENEMY, MOREENEMIES,EXIT, SPECIAL,BAK_TILE,DEFAULT,EXTRABOMBS,SNOWMAN, LESSBOMBS
    ));

    public static TextureRegion RandomSurprise() {
        return randomSurprise.get(random.nextInt(randomSurprise.size()));
//
    }

   public static void removeExit(){
        randomSurprise.remove(EXIT);
    }




}
