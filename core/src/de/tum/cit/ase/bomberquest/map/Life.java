package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Flowers are a static object without any special properties.
 * They do not have a hitbox, so the player does not collide with them.
 * They are purely decorative and serve as a nice floor decoration.
 */

public class Life implements Drawable {

    private final int x;
    private final int y;
    private final int lifeCounter;
    private float elapsedTime;

    public Life(int x, int y) {
        this.x = x;
        this.y = y;
        this.lifeCounter=Player.getLifeCounter();
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.HEART;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public void tick(float deltaTime) {
        elapsedTime += deltaTime;
    }


}


