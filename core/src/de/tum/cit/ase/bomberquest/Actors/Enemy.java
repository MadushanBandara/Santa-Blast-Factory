package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.Move;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.SpriteSheet;



/**
 * Represents an enemy character in the game.
 */
public class Enemy implements Drawable{

    private float x, y; // Position in grid coordinates
    private Move currentDirection; // Current movement direction
    private final TextureRegion texture; // Enemy's visual representation
    private float elapsedTime;
    private boolean isDead;
    public static int countEnemies;

    /** The Box2D hitbox of the Enemy, used for position and collision detection. */
    private final Body hitbox;
    /**
     * Constructs an Enemy instance with an initial position and direction.
     *
     * @param x         Initial X-coordinate in the grid.
     * @param y         Initial Y-coordinate in the grid.
     */
    public Enemy(World world, float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = SpriteSheet.ENEMIES.at(1, 1); // Default Grinch sprite
        this.currentDirection = randomDirection();
        this.hitbox = createHitbox(world, x, y);
        countEnemies= GameMap.getEnemiesGenerated();
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


    /**
     * Changes the direction to the opposite of the current direction.
     */

    public  void changeDirection() {
        Move newDirection;
        do {
            newDirection = randomDirection(); // Generate a random direction
        } while (newDirection == getCurrentDirection()); // ensure it is different from current enemy direction

        setCurrentDirection(newDirection); // Set the new direction
    }


    /**
     * Returns the current movement direction of the enemy.
     *
     * @return The current direction (UP, DOWN, LEFT, or RIGHT).
     */
    public Move getCurrentDirection() {
        return currentDirection;
    }


    /**
     * Chooses a random initial direction for the enemy.
     *
     //* @param vertical If true, restricts movement to UP and DOWN; otherwise, LEFT and RIGHT.
     * @return A random direction based on the vertical flag.
     */

    private Move randomDirection() {
        int pick = (int) (Math.random() * 4); // Randomly pick 0-3
        switch (pick) {
            case 0:
                return Move.UP;
            case 1:
                return Move.DOWN;
            case 2:
                return Move.LEFT;
            case 3:
                return Move.RIGHT;
            default:
                throw new IllegalStateException("Unexpected value");
        }
    }



    public void update(float deltaTime) {
        elapsedTime += deltaTime; // Update animation time
        // Set velocity based on current direction
        switch (currentDirection) {
            case UP:
                hitbox.setLinearVelocity(0, 1); // Move up
                break;
            case DOWN:
                hitbox.setLinearVelocity(0, -1); // Move down
                break;
            case LEFT:
                hitbox.setLinearVelocity(-1, 0); // Move left
                break;
            case RIGHT:
                hitbox.setLinearVelocity(1, 0); // Move right
                break;
        }

        // Randomly change direction with probability
        if (Math.random() < 0.001) {
            changeDirection();
        }
    }


    @Override
    public TextureRegion getCurrentAppearance() {
        if(!isDead){
            return Animations.ENEMY_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
        }
        // Get the frame of the walk down animation that corresponds to the current time.
        else
            MusicTrack.COLLECTING.play(false);
            return Animations.ENEMY_DEATH.getKeyFrame(this.elapsedTime, true);

        }



    public void killEnemy(){
        this.isDead=true;
        this.elapsedTime = 0;
        countEnemies--;
        Player.setTrackScore(Player.getTrackScore()+5);

        GameMap.setEnemiesGenerated(GameMap.getEnemiesGenerated() - 1);
    }

    public boolean isDeathAnimationFinished() {
        return isDead && Animations.ENEMY_DEATH.isAnimationFinished(elapsedTime);
    }



    public void stopMovement() {
        hitbox.setLinearVelocity(0, 0);// Stop any movement when no keys are pressed
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



    public boolean isDead() {
        return isDead;
    }

    public Body getHitbox() {
        return hitbox;
    }

    public void setCurrentDirection(Move currentDirection) {
        this.currentDirection = currentDirection;
    }


    /*code Inspired from Source code* https://github.com/Gaspared/Bomberman/blob/main/src/game/Enemy.java */

}


