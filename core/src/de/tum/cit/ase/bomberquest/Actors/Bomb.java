package de.tum.cit.ase.bomberquest.Actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

public class Bomb implements Drawable {

    private Vector2 position;
    private float timer;
    private static final float BOMB_LIFETIME = 3f; // Time until the bomb explodes

    public Bomb(float x, float y) {
        this.position = new Vector2(x, y);
        this.timer = BOMB_LIFETIME;
    }

    public void tick(float deltaTime) {
        timer -= deltaTime;
        if (timer <= 0) {
            // Trigger bomb explosion (this logic would need to be added later)
            explode();
        }
    }

    private void explode() {
        // Logic for bomb explosion (e.g., affect surrounding tiles, destroy enemies)
        System.out.println("Boom! Bomb exploded at: " + position);
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
