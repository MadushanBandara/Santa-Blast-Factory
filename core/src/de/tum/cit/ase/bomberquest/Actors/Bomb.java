package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.Tile;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * Represents Bombs in the game.
 * references:
 * https://www.geeksforgeeks.org/multidimensional-arrays-in-java/
 * https://www.gamedev.net/articles/programming/general-and-gameplay-programming/object-oriented-programming-in-games-r4918/
 * http://www.java-gaming.org/index.php?topic=25627.0
 * https://www.baeldung.com/java-2d-collision-detection
 * https://youtu.be/oPzPpUcDiYY?si=B2uTKlQpznkGUNeo
 * https://youtu.be/tcH6Mp03KC0?si=2T-MdwoduYQPPzMo
 */

public class Bomb implements Drawable {

    private Vector2 position;
    private float timer;
    private float explosionTimer;
    private static final float BOMB_LIFETIME = 3f; // Time until the bomb explodes
    private static final float EXPLOSION_LIFETIME = 1f; // Duration of explosion animation
    private boolean exploded;
    private static int maxBombs = 50;
    private static int EXPLOSION_RADIUS = 1; // Tiles affected in each direction
    private GameMap map;
    private Music music;

    public Bomb(float x, float y, GameMap map) {
        this.position = new Vector2(x, y);
        this.timer = BOMB_LIFETIME;
        this.explosionTimer = EXPLOSION_LIFETIME;
        this.exploded = false;
        this.map = map;
    }

    public void tick(float deltaTime) {
        if (!exploded) {
            // Countdown until explosion
            timer -= deltaTime;
            if (timer <= 0) {
                explode(map);
            }
        } else {
            // Countdown for explosion animation
            explosionTimer -= deltaTime;
        }
    }


    /**
     * Triggers the bomb explosion and handles its effects.
     * Reduces the player's bomb count and marks the bomb as exploded.
     * Damages the player if they are within the explosion radius and unprotected.
     * Kills enemies within the explosion radius, provided there are no blocking walls.
     * Handles the explosion effect on nearby tiles, such as breaking destructible walls.
     */


    public void explode(GameMap map) {
        if (getMaxBombs() == 0) {
            Player.outOfBombs();
            return;
        }

        exploded = true;
        explosionTimer = EXPLOSION_LIFETIME;
        setMaxBombs(getMaxBombs()-1);

        System.out.println("Explosion triggered at: (" + Math.round(position.x) + ", " + Math.round(position.y) + ")");

        int centerX = Math.round(position.x);
        int centerY = Math.round(position.y);

        Player player = map.getPlayer();


        tileExplosion(map, centerX, centerY);

        if (player != null && player.isAlive()) {
            int playerX = Math.round(player.getX());
            int playerY = Math.round(player.getY());

            // Check if player is in direct line and explosion is not blocked by unbreakable tile
            if ((playerX == centerX && Math.abs(playerY - centerY) <= EXPLOSION_RADIUS) ||
                    (playerY == centerY && Math.abs(playerX - centerX) <= EXPLOSION_RADIUS)) {
                // ensure no unbreakable tile between bomb and player
                if (!isExplosionBlockedByWall(map, centerX, centerY, playerX, playerY)) {
                    player.PlayerDied(); // Kill the player
                    System.out.println("Player killed by the bomb at (" + playerX + ", " + playerY + ")");
                }

            }
        }

        // Check for nearby enemies (only if there are any)
        if (!map.getEnemies().isEmpty()) {
            for (Enemy enemy : map.getEnemies()) {
                if (!enemy.isDead()) {
                    int enemyX = Math.round(enemy.getX());
                    int enemyY = Math.round(enemy.getY());

                    if ((enemyX == centerX && Math.abs(enemyY - centerY) <= EXPLOSION_RADIUS) ||
                            (enemyY == centerY && Math.abs(enemyX - centerX) <= EXPLOSION_RADIUS)) {
                        enemy.killEnemy(); // Kill the enemy
                        System.out.println("Enemy killed by the bomb at (" + enemyX + ", " + enemyY + ")");
                    }
                }
            }
        } else {
            System.out.println("No enemies to check in the explosion radius.");
        }
    }

    /**
     * Checks if an explosion is blocked by an indestructible wall between the bomb and a target.
     * Determines the direction of the target relative to the bomb's position.
     * Iterates through tiles in the path between the bomb and the target to check for obstacles.
     * Returns true if an indestructible wall is found, otherwise false.
     */

    private boolean isExplosionBlockedByWall(GameMap map, int bombX, int bombY, int targetX, int targetY) {
        // Determine direction
        int dx = Integer.compare(targetX, bombX);
        int dy = Integer.compare(targetY, bombY);

        // Check tiles between bomb and target
        int x = bombX + dx;
        int y = bombY + dy;
        while (x != targetX || y != targetY) {
            Tile tile = map.getTileAt(x, y);
            if (tile != null && tile.getTileType() == Tile.INDESTRUCTIBLE_WALL) {
                return true; // Explosion is blocked
            }
            x += dx;
            y += dy;
        }
        return false; // No blocking wall found
    }

    /**
     * Handles the explosion effect on tiles within the explosion radius.
     * Explodes breakable tiles in all four cardinal directions (up, down, left, right).
     * Stops the explosion in a direction if an indestructible wall is encountered.
     * Skips processing if a tile is null (out of bounds).
     */

    private void tileExplosion(GameMap map, int centerX, int centerY) {
        int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}
        };

        for (int[] direction : directions) {
            for (int i = 1; i <= EXPLOSION_RADIUS; i++) {
                int x = centerX + direction[0] * i;
                int y = centerY + direction[1] * i;

                Tile tile = map.getTileAt(x, y);
                if (tile != null) {
                    if (tile.isBreakable()) {
                        tile.explode();
                    } else if (tile.getTileType() == Tile.INDESTRUCTIBLE_WALL) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public TextureRegion getCurrentAppearance () {
        if (!exploded) {
            // Render bomb animation before explosion
            return Animations.BOMB.getKeyFrame(timer, false);
        } else if (explosionTimer > 0) {
            // Render explosion animation during its timer
            MusicTrack.EXPLOSION.play(false);
            return Animations.EXPLOSION.getKeyFrame(EXPLOSION_LIFETIME - explosionTimer, true);
        }
        return null; // No texture after explosion finishes
    }

        public float getX () {
            return position.x;
        }

        public float getY () {
            return position.y;
        }

        public boolean isExpired () {
            // The bomb is expired after the explosion animation ends
            return exploded && explosionTimer <= 0;
        }

        public boolean isExploded () {
            return exploded;
        }

        public static int getMaxBombs () {
            return maxBombs;
        }

        public Vector2 getPosition () {
            return position;
        }

        public static int getExplosionRadius () {
            return EXPLOSION_RADIUS;
        }

        public static void setExplosionRadius ( int explosionRadius){
            EXPLOSION_RADIUS = explosionRadius;
        }

        public static void setMaxBombs ( int maxBombs){
            Bomb.maxBombs = maxBombs;
        }

    public static void resetBombs() {

        maxBombs = 50;// Reset the bomb count
        setExplosionRadius(1);
    }
}
