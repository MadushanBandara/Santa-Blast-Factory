package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.Enemy;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.Tile;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

import static de.tum.cit.ase.bomberquest.Actors.Player.*;

public class Bomb implements Drawable {

    private static Vector2 position;
    private float timer;
    private float explosionTimer;
    private static final float BOMB_LIFETIME = 3f; // Time until the bomb explodes
    private static final float EXPLOSION_LIFETIME = 1f; // Duration of explosion animation
    private static boolean exploded;
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


    public void explode(GameMap map) {
        if (getMaxBombs() == 0) {
            Player.outOfBombs();
            System.out.println("No bombs left to drop!");
            return;
        }

        exploded = true;
        explosionTimer = EXPLOSION_LIFETIME;
        setMaxBombs(getMaxBombs()-1);

        System.out.println("Explosion triggered at: (" + Math.round(position.x) + ", " + Math.round(position.y) + ")");

        int centerX = Math.round(position.x);
        int centerY = Math.round(position.y);

        Player player = map.getPlayer();

        // Always handle tile explosions regardless of enemies
        tileExplosion(map, centerX, centerY);

        if (player != null && player.isAlive()) {
            int playerX = Math.round(player.getX());
            int playerY = Math.round(player.getY());

            // Ensure the player is within the radius and aligned horizontally or vertically
            if ((playerX == centerX && Math.abs(playerY - centerY) <= EXPLOSION_RADIUS) ||
                    (playerY == centerY && Math.abs(playerX - centerX) <= EXPLOSION_RADIUS)) {
                player.PlayerDied(); // Kill the player
                System.out.println("Player killed by the bomb at (" + playerX + ", " + playerY + ")");
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

    public static boolean isExploded () {
        return exploded;
    }

    public static int getMaxBombs () {
        return maxBombs;
    }

    public static Vector2 getPosition () {
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

        maxBombs =50 ;// Reset the bomb count
        setExplosionRadius(1);
    }
}
