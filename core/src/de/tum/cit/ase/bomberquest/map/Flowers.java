
package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Flowers are a static object without any special properties.
 * They do not have a hitbox, so the player does not collide with them.
 * They are purely decorative and serve as a nice floor decoration.
 */

/*
we kept the flower class and just changed the texture to be simply green
for an easier on the eye texture as we used
as a background for all other possible textures, if they do not cover the full tile)
 */
public class Flowers implements Drawable {

    private final int x;
    private final int y;

    public Flowers(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.FLOWERS;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}


