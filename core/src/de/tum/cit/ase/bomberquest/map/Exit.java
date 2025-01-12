package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.Actors.Santa;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;
import de.tum.cit.ase.bomberquest.map.GameMap;

public class Exit implements Drawable {

    private final float x;
    private final float y;

    private static boolean exitFound;
    private boolean exitRevealed;
    private final Body hitbox;

    public Exit(World world, float x, float y) {
        this.x = x;
        this.y = y;
        // Since the hitbox never moves, and we never need to change it, we don't need to store a reference to it.
        this.hitbox = createHitbox(world, x, y);
    }

    /**
     * Create a Box2D body for the chest.
     * @param world The Box2D world to add the body to.
     */
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
        System.out.println("exit created at position"+x+" "+y);
        return body;
    }


    @Override
    public TextureRegion getCurrentAppearance() {
        if(!exitRevealed){
            return Textures.BREAK_TILE;
        }
        else return Textures.EXIT;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }


    //for Player collision with tile to "exit", finish the game
    //Player should have already found or "revealed" the exit through wall destruction
    //revelation mechanism is still to be implemented
    public boolean exitFound(){
          if (exitRevealed=true){
              exitFound=true;
          }
       return exitFound;
    }

    //for when the player breaks the destructible tile to reveal the Exit
    public boolean ExitRevealed(GameMap map){
        Tile tile = map.getTileAt(x, y);
        if(tile.isExploded()){
            exitRevealed=true;
        }
        System.out.println("exit is revealed");

        return exitRevealed;
    }

    public boolean isExitRevealed() {
        return exitRevealed;
    }

    public static boolean isExitFound() {
        return exitFound;
    }

}
