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

    private Vector2 position;
    private float timer;
    private float explosionTimer;
    private static final float BOMB_LIFETIME = 3f; // Time until the bomb explodes
    private static final float EXPLOSION_LIFETIME = 1f; // Duration of explosion animation
    private static boolean exploded;
    private static int maxBombs=30;
    private static final int EXPLOSION_RADIUS = 1; // Tiles affected in each direction

    private Music music;

    public Bomb(float x, float y) {
        this.position = new Vector2(x, y);
        this.timer = BOMB_LIFETIME;
        this.explosionTimer = EXPLOSION_LIFETIME;
        this.exploded = false;
    }

    public void tick(float deltaTime) {
        if (!exploded) {
            // Countdown until explosion
            timer -= deltaTime;
            if (timer <= 0) {
                explode();
            }
        } else {
            // Countdown for explosion animation
            explosionTimer -= deltaTime;
        }
    }

    private void explode() {
        exploded = true;
        explosionTimer = EXPLOSION_LIFETIME;// Start explosion animation timer
        maxBombs--;
        System.out.println("Boom! Explosion triggered at: " + position+"remaining bombs"+maxBombs);
        //Explosion logic based on radius
        /*
        for (int x = Math.max(0, (int) position.x - EXPLOSION_RADIUS);
             x <= Math.min(map.getWidth() - 1, position.x + EXPLOSION_RADIUS); x++) {
            for (int y = Math.max(0, (int) position.y - EXPLOSION_RADIUS);
                 y <= Math.min(map.getHeight() - 1, position.y + EXPLOSION_RADIUS); y++) {
                Tile tile = map.getTileAt(x, y);
                if (tile != null) {
                    tile.explode();
                } else {
                    System.out.println("No tile at (" + x + ", " + y + ")");
                }
            }

        }*/
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
}
