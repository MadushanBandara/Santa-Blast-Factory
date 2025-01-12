package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;


/**
 * A chest is a static object with a hitbox, so the player cannot walk through it.
 */
public class Chest implements Drawable {
    
    // We would normally get the position from the hitbox, but since we don't need to move the chest, we can store the position directly.
    private final float x;
    private final float y;
    private boolean IsOpened;
    private final Body hitbox;
    
    /**
     * Create a chest at the given position.
     * @param world The Box2D world to add the chest's hitbox to.
     * @param x The X position.
     * @param y The Y position.
     */
    public Chest(World world, float x, float y) {
        this.x = x;
        this.y = y;
        // Since the hitbox never moves, and we never need to change it, we don't need to store a reference to it.
        this.hitbox = createHitbox(world, x, y);
    }
    
    /**
     * Create a Box2D body for the chest.
     * @param world The Box2D world to add the body to.
     */
    /*
    private Body createHitbox(World world, float x, float y) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Static bodies never move, but static bodies can collide with them.
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Set the initial position of the body.
        //bodyDef.position.set(this.x, this.y);
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
        return body;
    }*/
    private Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a circle shape for the player.
        CircleShape circle = new CircleShape();
        // Give the circle a radius of 0.3 tiles (the player is 0.6 tiles wide).
        circle.setRadius(0.3f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the player.
        body.createFixture(circle, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        circle.dispose();
        // Set the player as the user data of the body so we can look up the player from the body later.
        body.setUserData(this);
        return body;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if(!ChestOpened()){
            return Textures.CHEST;
        }
        else return Textures.OPENEDCHEST;
    }
    
    @Override
    public int getX() {
        return (int) x;
    }
    
    @Override
    public int getY() {
        return (int) y;
    }

    public boolean ChestOpened() {
        IsOpened=true;
        return true;
    }

    public boolean isOpened() {
        return IsOpened;
    }

}
