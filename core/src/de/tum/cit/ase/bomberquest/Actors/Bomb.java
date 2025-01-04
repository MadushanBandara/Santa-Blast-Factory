package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

public class Bomb implements Drawable {

    private Vector2 position;
    private float timer;
    private float explosionTimer;
    private static final float BOMB_LIFETIME = 3f; // Time until the bomb explodes
    private static final float EXPLOSION_LIFETIME = 1f; // Duration of explosion animation
    private boolean exploded;

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
        explosionTimer = EXPLOSION_LIFETIME; // Start explosion animation timer
        System.out.println("Boom! Explosion triggered at: " + position);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if (!exploded) {
            // Render bomb animation before explosion
            return Animations.BOMB.getKeyFrame(timer, true);
        } else if (explosionTimer > 0) {
            // Render explosion animation during its timer
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
}
