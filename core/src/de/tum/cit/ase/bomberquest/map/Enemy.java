package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.SpriteSheet;
import de.tum.cit.ase.bomberquest.texture.Textures;
import com.badlogic.gdx.physics.box2d.World;


/*Source code* https://github.com/Gaspared/Bomberman/blob/main/src/game/Enemy.java */

/**
 * Represents an enemy character in the game.
 */
public class Enemy implements Drawable{

    private int x, y; // Position in grid coordinates
    private Move currentDirection; // Current movement direction
    private final TextureRegion texture; // Enemy's visual representation
    private float elapsedTime;

    /** The Box2D hitbox of the player, used for position and collision detection. */
    private final Body hitbox;
    /**
     * Constructs an Enemy instance with an initial position and direction.
     *
     * @param x         Initial X-coordinate in the grid.
     * @param y         Initial Y-coordinate in the grid.
     * @param vertical  If true, restricts movement to vertical directions (UP, DOWN).
     */
    public Enemy(World world, int x, int y, boolean vertical) {
        this.x = x;
        this.y = y;
        this.texture = SpriteSheet.ENEMIES.at(1, 1); // Default Grinch sprite
        this.currentDirection = randomDirection(vertical);
        this.hitbox = createHitbox(world, x, y);
    }

    /**
     * Changes the direction to the opposite of the current direction.
     */
    public void changeDirection() {
        if (currentDirection == Move.DOWN) {
            currentDirection = Move.UP;
        } else if (currentDirection == Move.UP) {
            currentDirection = Move.DOWN;
        } else if (currentDirection == Move.LEFT) {
            currentDirection = Move.RIGHT;
        } else {
            currentDirection = Move.LEFT;
        }
    }

    /**
     * Returns the current movement direction of the enemy.
     *
     * @return The current direction (UP, DOWN, LEFT, or RIGHT).
     */
    public Move getCurrentDirection() {
        return currentDirection;
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
     * Chooses a random initial direction for the enemy.
     *
     * @param vertical If true, restricts movement to UP and DOWN; otherwise, LEFT and RIGHT.
     * @return A random direction based on the vertical flag.
     */

    private Move randomDirection(boolean vertical) {
        int pick = (int) (Math.random() * 2); // Randomly pick 0 or 1
        if (vertical) {
            return pick == 0 ? Move.UP : Move.DOWN;
        } else {
            return pick == 0 ? Move.LEFT : Move.RIGHT;
        }
    }




    /*Updates the enemy's position based on its current direction.
            */
    /*
    public void update() {
        // Move the enemy based on its current direction
        switch (currentDirection) {
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }

        // Randomly change direction with a 10% chance
        if (Math.random() < 0.1) {
            changeDirection();
        }
    }
    */
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

        // Randomly change direction with a small chance
        if (Math.random() < 0.05) {
            changeDirection();
        }
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
        return Animations.ENEMY_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
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
}


