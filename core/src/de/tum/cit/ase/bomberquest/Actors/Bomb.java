package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.Tile;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

public class Bomb implements Drawable {

    private static Vector2 position;
    private float timer;
    private float explosionTimer;
    private static final float BOMB_LIFETIME = 3f; // Time until the bomb explodes
    private static final float EXPLOSION_LIFETIME = 1f; // Duration of explosion animation
    private static boolean exploded;
    private static int maxBombs=30;
    private static final int EXPLOSION_RADIUS = 1; // Tiles affected in each direction
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



    private void explode(GameMap map) {
        exploded = true;
        explosionTimer = EXPLOSION_LIFETIME; // Start explosion animation timer
        maxBombs--;
        System.out.println("Boom! Explosion triggered at: (" + Math.round(position.x) + ", " + Math.round(position.y) + ") remaining bombs " + maxBombs);

        // Calculate the tile coordinates for the bomb's position
        int centerX = Math.round(position.x);
        int centerY = Math.round(position.y);

        Player player = map.getPlayer();

        if (player != null && player.isAlive()) {
            int playerX = Math.round(player.getX());
            int playerY = Math.round(player.getY());

            // Check the player's position against the bomb's center and its explosion radius
            if (Math.abs(playerX - centerX) <= EXPLOSION_RADIUS && Math.abs(playerY - centerY) <= EXPLOSION_RADIUS) {
                player.PlayerDied(); // Kill the player
                System.out.println("Player killed by the bomb at (" + playerX + ", " + playerY + ")");
            }
        }

        // Explosion logic based on radius
        // Directions for explosion (up, down, left, right)
        int[][] directions = {
                {0, 1},  // Up
                {0, -1}, // Down
                {1, 0},  // Right
                {-1, 0}  // Left
        };

        // Iterate through each direction to check the nearest breakable tile within the explosion radius
        for (int[] direction : directions) {
            for (int i = 1; i <= EXPLOSION_RADIUS; i++) {
                int x = centerX + direction[0] * i;
                int y = centerY + direction[1] * i;

                // Ensure the coordinates are within map bounds
                if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
                    break; // Stop if out of bounds
                }

                Tile tile = map.getTileAt(x, y);
                if (tile != null) {
                    if (tile.isBreakable()) {
                        // Explode the first breakable tile and stop further propagation in this direction
                        tile.explode();
                        System.out.println("Tile at (" + x + ", " + y + ") exploded.");
                        break; // Stop after affecting the first breakable tile in any direction
                    } else if (tile.getTileType() == Tile.INDESTRUCTIBLE_WALL) {
                        // Stop propagation if an indestructible wall is encountered
                        break;
                    }
                }
            }
        }
    }



    @Override
    public TextureRegion getCurrentAppearance() {
        if (!exploded) {
            // Render bomb animation before explosion
            return Animations.BOMB.getKeyFrame(timer, false);
        } else if (explosionTimer > 0) {
            // Render explosion animation during its timer
            MusicTrack.EXPLOSION.play();
            return Animations.EXPLOSION.getKeyFrame(EXPLOSION_LIFETIME - explosionTimer, true);
        }
        return null; // No texture after explosion finishes
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public boolean isExpired() {
        // The bomb is expired after the explosion animation ends
        return exploded && explosionTimer <= 0;
    }

    public static boolean isExploded() {
        return exploded;
    }

    public static int getMaxBombs() {
        return maxBombs;
    }

    public static Vector2 getPosition() {
        return position;
    }
}