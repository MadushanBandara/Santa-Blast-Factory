package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * Represents a bomb in the game.
 * The bomb has a timer and explodes after a certain time.
 */
public class Bomb implements Drawable {

    private final Vector2 position;
    private float timer;
    private static final float BOMB_LIFETIME = 3f; // Time until the bomb explodes

    public Bomb(float x, float y) {
        this.position = new Vector2(x, y);
        this.timer = BOMB_LIFETIME;
    }

    /**
     * Updates the bomb's timer and triggers the explosion if the timer reaches zero.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    public void tick(float deltaTime) {
        timer -= deltaTime;
        if (timer <= 0) {
            explode();
        }
    }

    /**
     * Triggers the explosion logic.
     * This can include affecting surrounding tiles, damaging entities, etc.
     */
    private void explode() {
        System.out.println("Boom! Bomb exploded at: " + position);
        // Add explosion logic here (e.g., damage surrounding tiles)
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        return Animations.BOMB.getKeyFrame(timer, true);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public boolean isExpired() {
        return timer <= 0;
    }
}
