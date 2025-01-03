package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Represents a single tile on the game map.
 */
public class Tile implements Drawable {

    public static final int INDESTRUCTIBLE_WALL = 0;
    public static final int DESTRUCTIBLE_WALL = 1;
    public static final int EMPTY = 9;
    public static final int ENTRANCE = 2;
    public static final int SPECIAL = 5;
    public static final int POWERUP = 6;

    private final float x;
    private final float y;
    private final int tileType;


    public Tile(World world,float x, float y, int tileType) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
        createHitbox(world);
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
        return Textures.getTextureForTileType(tileType);
    }

    private void createHitbox(World world) {
        if (tileType == 1 || tileType == 0) {
            // These tiles are traversable, so we skip creating a hitbox


        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Static bodies never move, but static bodies can collide with them.
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Set the initial position of the body.
        bodyDef.position.set(this.x, this.y);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a polygon shape for the chest.
        PolygonShape box = new PolygonShape();
        // Make the polygon a square with a side length of 1 tile.
        box.setAsBox(0.5f, 0.5f);
        // Attach the shape to the body as a fixture.
        body.createFixture(box, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        box.dispose();
        // Set the chest as the user data of the body so we can look up the chest from the body later.
        body.setUserData(this);
    }
    }

}
