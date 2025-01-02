package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player implements Drawable {

    private float elapsedTime; // Total time elapsed since the game started
    private final Body hitbox; // Box2D hitbox of the player
    private Direction currentDirection; // Current movement direction

    public Player(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
        this.currentDirection = Direction.IDLE; // Start as idle
    }

    /**
     * Creates a Box2D body for the player.
     * This is what the physics engine uses to move the player around and detect collisions.
     *
     * @param world  The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body.
     */
    private Body createHitbox(World world, float startX, float startY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startX, startY);

        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.3f);
        body.createFixture(circle, 1.0f);
        circle.dispose();

        body.setUserData(this);
        return body;
    }

    /**
     * Updates the player's state, including movement and animation.
     *
     * @param frameTime Time since the last frame.
     */
    public void tick(float frameTime) {
        this.elapsedTime += frameTime;

        // Movement logic
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveDown();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight();
        } else {
            stopMovement();
        }
    }

    /**
     * Directional movement methods.
     */
    public void moveUp() {
        hitbox.setLinearVelocity(0, 3f);
        currentDirection = Direction.UP;
    }

    public void moveDown() {
        hitbox.setLinearVelocity(0, -3f);
        currentDirection = Direction.DOWN;
    }

    public void moveLeft() {
        hitbox.setLinearVelocity(-3f, 0);
        currentDirection = Direction.LEFT;
    }

    public void moveRight() {
        hitbox.setLinearVelocity(3f, 0);
        currentDirection = Direction.RIGHT;
    }

    public void stopMovement() {
        hitbox.setLinearVelocity(0, 0);
        currentDirection = Direction.IDLE;
    }

    /**
     * Returns the player's current animation based on direction.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        switch (currentDirection) {
            case UP:
                return Animations.CHARACTER_WALK_UP.getKeyFrame(elapsedTime, true);
            case DOWN:
                return Animations.CHARACTER_WALK_DOWN.getKeyFrame(elapsedTime, true);
            case LEFT:
                return Animations.CHARACTER_WALK_LEFT.getKeyFrame(elapsedTime, true);
            case RIGHT:
                return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(elapsedTime, true);
            default:
                return Animations.CHARACTER_IDLE.getKeyFrame(elapsedTime, true);
        }
    }

    @Override
    public float getX() {
        return hitbox.getPosition().x;
    }

    @Override
    public float getY() {
        return hitbox.getPosition().y;
    }

    /**
     * Enum for player movement directions.
     */
    private enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE
    }
}
