package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.Actors.Bomb;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.screen.GameScreen;

import java.util.List;

import static de.tum.cit.ase.bomberquest.texture.Textures.*;


/**
 * Represents a single tile on the game map.
#*/

public class Tile implements Drawable {

    public static final int INDESTRUCTIBLE_WALL = 0;
    public static final int DESTRUCTIBLE_WALL = 1;
    public static final int EMPTY = 9;
    public static final int ENTRANCE = 2;
    public static final int SPECIAL = 5;
    public static final int POWERUP = 6;
    public static final int POWERDOWN=3;
    public static final int EXIT = 8;
    private float elapsedTime;

    private final float x;
    private final float y;
    private int tileType;
    private Body body;
    private boolean exploded;
    private boolean animationFinished;
    private TextureRegion currentAppearance;
    private static boolean exitFound=false;
    private static boolean exitRevealed=false;
    private boolean PowerupRedeemed;
    private boolean PowerupFound;



    public Tile(World world,float x, float y, int tileType) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
        this.body=createHitbox(world);
        this.currentAppearance = Textures.getTextureForTileType(tileType);
        this.PowerupRedeemed=false;
        this.PowerupFound=false;

    }
    @Override
    public float getX() {
        return x;
    }
    @Override
    public float getY() {
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

        @Override
        public TextureRegion getCurrentAppearance() {
            if (isExploded()) {
                assignRandomTextureAfterExplosion();
                if (animationFinished) {
                    return currentAppearance; // Return the final texture
                } else {
                    return Animations.WALLEXPLOSION.getKeyFrame(elapsedTime, false); // Animation texture
                }
            }
            return Textures.getTextureForTileType(tileType); // Default appearance for non-exploded tiles
        }

    public void assignRandomTextureAfterExplosion() {
        if (!animationFinished) {
            if (Animations.WALLEXPLOSION.isAnimationFinished(elapsedTime)) {
                animationFinished = true;
                currentAppearance = Textures.RandomSurprise(); // Assign the random texture
                setTileType(EMPTY);
                System.out.println("Random texture assigned: " + currentAppearance);
            }
        }
    }

    public void grantPowerup(Player player) {
        if (!isPowerupRedeemed()&& isPowerupFound()) {
            PowerupRedeemed = true;

            if (currentAppearance.equals(Textures.LIFE)) {
                player.setLifeCounter(player.getLifeCounter() + 1);
                GameMap.updateLifeCounter();
                if (player.getLifeCounter() == 3) {
                    removeLife(); // Ensure maximum of 3 lives total
                }
                System.out.println("Player gained an extra life!");
            } else if (currentAppearance.equals(Textures.EXIT)) {
                setTileType(EXIT);
                Textures.removeExit(); // Remove EXIT from surprise list
                setExitRevealed(true);
                System.out.println("Exit revealed!");
            } else if (currentAppearance.equals(Textures.BLASTRADIUSPLUS)) {
                Bomb.setExplosionRadius(Bomb.getExplosionRadius() + 1);
                if(Bomb.getExplosionRadius()==8) {
                    Textures.removeBlastRadius();
                }
                System.out.println("Explosion radius increased!");
            } else if (currentAppearance.equals(Textures.EXTRABOMBS)) {
                Bomb.setMaxBombs(Bomb.getMaxBombs() + 5);
                System.out.println("Extra bombs granted!");
            } else if (currentAppearance.equals(Textures.LESSBOMBS)) {
                Bomb.setMaxBombs(Bomb.getMaxBombs() - 5);
                System.out.println("Fewer bombs available!");
            }
            if(!currentAppearance.equals(Textures.EXIT) && !Textures.randomNonPU.contains(currentAppearance)){
                currentAppearance =Textures.randomNonPU();
            }

        }
    }

    private Body createHitbox(World world) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Static bodies for tiles
        bodyDef.position.set(this.x, this.y); // Set the initial position of the body

        // Create the body in the world using the body definition
        Body body = world.createBody(bodyDef);

        // Create a polygon shape for the tile
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f); // Half the width/height of a tile

        // Create the fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1.0f; // Adjust as needed

        // sensor based on tile type
        if (tileType == 1 || tileType == 0) {
            // Non-traversable
            fixtureDef.isSensor = false;
        } else
        {
            // Traversable
            fixtureDef.isSensor = true;
        }

        // Attach the fixture to the body
        body.createFixture(fixtureDef);

        // Clean up the shape object
        box.dispose();

        // Set the body’s UserData to the tile for collision handling
        body.setUserData(this);
        return body;
    }


    public boolean isBreakable() {
        return tileType == DESTRUCTIBLE_WALL;
    }
    public boolean isTraversable() {
        return tileType == EMPTY;// Traversable if the tile is of type EMPTY
    }
    public void explode() {
        if (isBreakable()) {
           // Change the tile to an empty tile
            setExploded(true);
            setTileType(EMPTY);
            for (Fixture fixture : body.getFixtureList()) { //Chatgpt help here
                fixture.setSensor(true); // Make the fixture a sensor
            }

            System.out.println("Tile at (" + x + ", " + y + ") exploded and became EMPTY.");
        } else {
            System.out.println("Tile at (" + x + ", " + y + ") is not destructible.");
        }
    }



    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public void tick(float deltaTime) {
        if (isExploded()) {
            elapsedTime += deltaTime;
        }
    }

    public static void setExitFound(boolean exitFound) {
        Tile.exitFound = exitFound;
    }

    public static boolean isExitFound() {
        return exitFound;
    }

    public static void setExitRevealed(boolean exitRevealed) {
        Tile.exitRevealed = exitRevealed;
    }

    public boolean isPowerupRedeemed() {
        return PowerupRedeemed;
    }

    public void setPowerupRedeemed(boolean powerupRedeemed) {
        PowerupRedeemed = powerupRedeemed;
    }

    public boolean isPowerupFound() {
        return PowerupFound;
    }

    public void setPowerupFound(boolean powerupFound) {
        this.PowerupFound = powerupFound;
    }
}


