package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;


public class Santa implements Drawable {

    private final Body hitbox;
    private static boolean isSaved;
    private boolean isAlive;
    private float elapsedTime;
    private float timer;

    public Santa(World world, float x, float y){
        this.hitbox = createHitbox(world, x, y);
        this.isAlive = true;
        this.isSaved = false;

    }

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
        if(!isAlive()){
            MusicTrack.GAMEOVER.play();
            return Textures.SANTA;
        }
        else {
        return  Textures.SANTA;
    }
    }
    @Override
    public int getX() {
        // The x-coordinate of the player is the x-coordinate of the hitbox (this can change every frame).
        return (int) hitbox.getPosition().x;
    }

    @Override
    public int getY() {
        // The y-coordinate of the player is the y-coordinate of the hitbox (this can change every frame).
        return (int) hitbox.getPosition().y;
    }
        public Body getHitbox() {
            return hitbox;
        }

        public boolean isAlive() {
            return isAlive;
        }

        public static boolean isSaved() {
        return isSaved;}

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void tick(float deltaTime) {
        elapsedTime += deltaTime; // Increment elapsed time for animations or timed logic

        if (!isSaved()) {
            timer -= deltaTime; // Decrease timer using delta time
            if (timer <= 0) {
                // Add relevant behavior here if needed, e.g., a warning or state change
            }
        }
    }

    public void SantaSaved(){
        isSaved=true;
        System.out.println("Woohoo, you Saved Santa");
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}