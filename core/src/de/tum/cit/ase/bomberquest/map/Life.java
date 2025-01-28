package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Lives are a static object without any special properties.
 * they are rendered as hearts on the edges of map to represent the life counter of the player
 * and do not collide or interact with other elements.
 * the Lives counter is made to be dynamic same as its map representation that will update
 * everytime a player loses or gains a life/
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


