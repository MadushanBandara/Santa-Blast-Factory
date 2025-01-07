package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;
import de.tum.cit.ase.bomberquest.texture.Drawable;
/*

public class IndestructibleWalls implements Drawable {
    private static final int TILE_SIZE = 16;
    private final int x;
    private final int y;

    public IndestructibleWalls (World world, int x, int y) {
        this.x = x;
        this.y = y;
        // Create the hitbox
        createHitbox(world);
    }

    private void createHitbox(World world) {
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

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.INDESTRUCTIBLEWALLS;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

}
*/