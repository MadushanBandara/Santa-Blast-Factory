package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.screen.GameScreen;
import de.tum.cit.ase.bomberquest.screen.Hud;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import java.util.ArrayList;
import static com.badlogic.gdx.math.Interpolation.circle;
import static de.tum.cit.ase.bomberquest.screen.GameScreen.SCALE;
import static de.tum.cit.ase.bomberquest.screen.GameScreen.TILE_SIZE_PX;

import java.util.ArrayList;



public class Santa implements Drawable {

    private final Body hitbox;
    private static boolean isSaved;
    private boolean isAlive;
    private float elapsedTime;
    private float timer;

    public Santa(World world, float x, float y){
        this.hitbox = createHitbox(world, x, y);
        this.isAlive = true;
        isSaved = false;

    }

    private Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        // Set the initial position of the body.
        bodyDef.position.set(2,2 );
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
            MusicTrack.GAMEOVER.play(false);
            return Textures.SANTA;
        }
        if(isSaved()){

            return Animations.SANTAEXIT.getKeyFrame(getElapsedTime(), true);
        }
        else {
        return  Textures.SANTA;

    }
    }
    @Override
    public float getX() {
        // The x-coordinate of the player is the x-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().x;
    }

    @Override
    public float getY() {
        // The y-coordinate of the player is the y-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().y;
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
        elapsedTime += deltaTime; //

        if (isSaved()) {
            timer -= deltaTime; // Decrease timer using delta time
            if (timer <= 0) {
                float X = hitbox.getPosition().x;
                // Stop when Santa is out of th map
                if (X >= 30) {
                    hitbox.setLinearVelocity(0, 0); // Stop movement

                }

            }
        }
    }

    public void SantaSaved(){
        isSaved=true;
        Hud.setWorldTimer(Hud.getWorldTimer()+50);//get Some Extra time when you save Santa
        hitbox.setLinearVelocity(2f, 1f);
        MusicTrack.HOHOHO.play(false);
        Player.setTrackScore(Player.getTrackScore()+10);
        System.out.println("Woohoo, you Saved Santa");
    }


    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }





}