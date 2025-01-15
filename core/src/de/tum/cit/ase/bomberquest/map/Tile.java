package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


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
    private float elapsedTime;

    private final float x;
    private final float y;
    private int tileType;
    private Body body;
    private boolean exploded;




    public Tile(World world,float x, float y, int tileType) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
        this.body=createHitbox(world);
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
        if(isExploded()){

            return Animations.WALLEXPLOSION.getKeyFrame(elapsedTime, false);
        }

        else return Textures.getTextureForTileType(tileType);
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




}


