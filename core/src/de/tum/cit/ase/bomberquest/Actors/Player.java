package de.tum.cit.ase.bomberquest.Actors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.GameStatus;
import de.tum.cit.ase.bomberquest.map.Tile;
import de.tum.cit.ase.bomberquest.screen.Hud;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import java.util.ArrayList;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player implements Drawable {

    /** Total time elapsed since the game started. We use this for calculating the player movement and animating it. */
    private float elapsedTime;


    /** The Box2D hitbox of the player, used for position and collision detection. */
    private final Body hitbox;

    /** Player's life status. */
    private static boolean isAlive;

    private static int lifeCounter=1;

    private ArrayList<Bomb> bombs;

    /** Whether the player can drop a bomb. */
    private static boolean canDropBomb;

    /** Number of enemies defeated. */
    private int enemiesDefeated;

    /** Whether the exit is unlocked. */
    private boolean isExitUnlocked;

    private float deathAnimationTime = 0f;
    private static float WinAnimationTime = 0f;
    private static boolean playerWon=false;
    private static boolean playerSurvived=false;
    private float survivalTime = 0f;
    private final GameMap map;

    private float speed = 4f;
    private boolean runPowerupActive = false;

    private static int trackScore=0;
    private static boolean scoreCalculated=false;

    public Player(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
        this.isAlive = true;
        bombs = new ArrayList<>();
        this.canDropBomb = true; // Starts with the ability to drop one bomb
        this.enemiesDefeated = 0;
        this.isExitUnlocked = false;
        this.map = GameMap.getMap();
        trackScore=0;
    }

    public static int getLifeCounter() {
        return lifeCounter;
    }

    public static void setLifeCounter(int lifeCounter) {
        Player.lifeCounter = lifeCounter;
    }

    public static int getTrackScore() {
        return trackScore;
    }

    public static void setTrackScore(int trackScore) {
        Player.trackScore = trackScore;
    }

    /**
     * Creates a Box2D body for the player.
     * This is what the physics engine uses to move the player around and detect collisions with other bodies.
     * @param world The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body.
     */
    private Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(10,10);
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
     * Move the player around in a circle by updating the linear velocity of its hitbox every frame.
     * This doesn't actually move the player, but it tells the physics engine how the player should move next frame.
     * @param frameTime the time since the last frame.
     */
    public void tick(float frameTime, GameMap map) {
        this.elapsedTime += frameTime;


        if (!isAlive && deathAnimationTime > 0) {
            deathAnimationTime -= frameTime;
        }
        if(GameStatus.GameWon() && WinAnimationTime > 0){
            WinAnimationTime -= frameTime;

        }

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

        if (!isAlive) {
            stopMovement();
        }

        // Handle key press for bomb drop
        handleKeyPress(map);

        // Update all bombs
        bombs.forEach(bomb -> bomb.tick(frameTime));
        bombs.removeIf(bomb -> {
            boolean expired = bomb.isExpired();
            if (expired) canDropBomb = true;
            return expired;
        });

        // Remove expired bombs
        bombs.removeIf(Bomb::isExpired);


    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if(!isAlive()){
            MusicTrack.GAMEOVER.play(false);
            return Animations.CHARACTER_DEATH.getKeyFrame(elapsedTime, true);
        }
        else if(GameStatus.GameWon()){
            MusicTrack.WIN.play(false);
            return Animations.CHARACTER_WIN.getKeyFrame(WinAnimationTime, true);
        }
        else {
            switch (currentDirection) {
                case UP:
                    return Animations.CHARACTER_WALK_UP.getKeyFrame(elapsedTime, true);
                case DOWN:
                    return Animations.CHARACTER_WALK_DOWN.getKeyFrame(elapsedTime, true);
                case LEFT:
                    return Animations.CHARACTER_WALK_LEFT.getKeyFrame(elapsedTime, true);
                case RIGHT:
                    return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(elapsedTime, true);
                default: // IDLE
                    return Animations.CHARACTER_IDLE.getKeyFrame(elapsedTime, true);
            }
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

    private enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE
    }

    private Direction currentDirection = Direction.IDLE;


    public void moveUp() {
        hitbox.setLinearVelocity(0, speed);
        currentDirection = Direction.UP;// Move upwards
    }

    public void moveDown() {
        hitbox.setLinearVelocity(0, -speed);
        currentDirection = Direction.DOWN;// Move downwards
    }

    public void moveLeft() {
        hitbox.setLinearVelocity(-speed, 0);
        currentDirection = Direction.LEFT;// Move left
    }

    public void moveRight() {
        hitbox.setLinearVelocity(speed, 0);
        currentDirection = Direction.RIGHT;// Move right
    }

    public void stopMovement() {
        hitbox.setLinearVelocity(0, 0);
        currentDirection = Direction.IDLE;// Stop any movement when no keys are pressed
    }

    // Method to handle key presses (you already have this in the tick method)
    public void handleKeyPress(GameMap map) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && canDropBomb) {
            MusicTrack.BOMBDROPSOUND.play(false);

            dropBomb(map);
        }
    }


    private void dropBomb(GameMap map) {
        if(Bomb.getMaxBombs()>0) {
            // Create a new bomb at the player's position
            Bomb bomb = new Bomb(getX(), getY(), map);
            bombs.add(bomb);

            // Set canDropBomb to false until the bomb is dropped and exploded
            canDropBomb = false;
        }
        else return;
    }

    public void render(SpriteBatch spriteBatch) {
        // Render the player as before
        // Render all bombs
        for (Bomb bomb : bombs) {
            spriteBatch.draw(bomb.getCurrentAppearance(), bomb.getX(), bomb.getY());
        }
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void PlayerDied() {

        if (lifeCounter > 1) {
            lifeCounter--;
            playerSurvived(map); // Reset player position
            System.out.println("Player died. Lives remaining: " + lifeCounter);

        } else {
            System.out.println("Game Over! No extra lives left.");
            setIsAlive(false); // Mark the player as dead
            deathAnimationTime = 5f; // Trigger the death animation
            MusicTrack.GAMEOVER.play(false);
            lifeCounter--;
            removeSpeedRun();
            trackScore=0;
        }

    }

    public void playerSurvived(GameMap map) {

        map=GameMap.getMap();
        // Ensure any physics modifications are deferred to avoid conflicts
        map.addDeferredAction(() -> { //ChatGpt help ONLY with the deferred action method
            //Same methods as reset method
            hitbox.setTransform(10, 10, 0); // Reset position to starting coordinates
            hitbox.setLinearVelocity(0, 0); // Stop movement
            hitbox.setAngularVelocity(0);  // Stop rotation
        } );

        System.out.println("Player position reset");
    }

    public void removeSpeedRun() {
        if (lifeCounter > 0) {
            setSpeed(3f);
            setRunPowerupActive(false);// Deactivate the speed power-up
            System.out.println("Speed run power-up has been removed.");
        }
    }

    public static void PlayerWon()
    {
        playerWon=true;
        WinAnimationTime = 5f;
        if(!scoreCalculated){
            int score=finalScore();
            Player.setTrackScore(score);
        }

    }


    public static int finalScore(){
        int score;
        score=(Player.getTrackScore()+Hud.getWorldTimer());
        setScoreCalculated(true);
        return score;
    }

    public static void setIsAlive(boolean isAlive) {
        Player.isAlive = isAlive;
    }

    public static boolean isIsAlive() {
        return isAlive;
    }

    public static void PlayerGrantedPowerUP(){

        System.out.println("player is granted a Gift");
    }

    public Body getHitbox() {
        return hitbox;
    }

    public static boolean isAlive() {
        return isAlive;
    }


    public static boolean outOfBombs(){
        if(Bomb.getMaxBombs()==0){
            setCanDropBomb(false);
            System.out.println("You ran out of Bombs :(");
        } 
        return canDropBomb;
    }

    public static float getWinAnimationTime() {
        return WinAnimationTime;
    }

    public static void setCanDropBomb(boolean canDropBomb) {
        Player.canDropBomb = canDropBomb;
    }

    public float getDeathAnimationTime() {
        return deathAnimationTime;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setRunPowerupActive(boolean runPowerupActive) {
        this.runPowerupActive = runPowerupActive;
    }

    public void reset(World world, float startX, float startY) {
        this.isAlive = true;
        this.bombs.clear();
        Bomb.resetBombs();
        this.deathAnimationTime = 0;
        this.canDropBomb = true; // Reset bomb-dropping ability
        this.enemiesDefeated = 0;
        this.isExitUnlocked = false;
        this.setSpeed(3);
        trackScore=0;

        GameStatus.reset();


        // Reset life counter //not yet functional
        this.lifeCounter = 1;

        // Reset hitbox position
        hitbox.setTransform(startX, startY, 0);
        hitbox.setLinearVelocity(0, 0);
        hitbox.setAngularVelocity(0);
        System.out.println("Player has been reset.");
    }

    public static boolean isPlayerSurvived() {
        return playerSurvived;
    }

    public float getSurvivalTime() {
        return survivalTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public static void setPlayerSurvived(boolean playerSurvived) {
        Player.playerSurvived = playerSurvived;
    }

    public static boolean isScoreCalculated() {
        return scoreCalculated;
    }

    public static void setScoreCalculated(boolean scoreCalculated) {
        Player.scoreCalculated = scoreCalculated;
    }
}
