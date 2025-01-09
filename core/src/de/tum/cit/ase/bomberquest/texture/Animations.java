package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all animation constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Animations {
    
    /**
     * The animation for the character walking down.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 1),
            SpriteSheet.CHARACTER.at(1, 2),
            SpriteSheet.CHARACTER.at(1, 3),
            SpriteSheet.CHARACTER.at(1, 4)
    );

    public static final Animation<TextureRegion> CHARACTER_DEATH = new Animation<>(0.1f,
            SpriteSheet.CHARACTERDEATH.at(9, 1),
            SpriteSheet.CHARACTERDEATH.at(9, 2),
            SpriteSheet.CHARACTERDEATH.at(9, 3),
            SpriteSheet.CHARACTERDEATH.at(9, 5),
            SpriteSheet.CHARACTERDEATH.at(9, 6),
            SpriteSheet.CHARACTERDEATH.at(9, 8),
            SpriteSheet.CHARACTERDEATH.at(9, 9),
            SpriteSheet.CHARACTERDEATH.at(9, 12)

    );


    /**
     * Animation for the character walking up.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_UP = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(3, 1),
            SpriteSheet.CHARACTER.at(3, 2),
            SpriteSheet.CHARACTER.at(3, 3),
            SpriteSheet.CHARACTER.at(3, 4)
    );

    /**
     * Animation for the character walking left.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(4, 1),
            SpriteSheet.CHARACTER.at(4, 2),
            SpriteSheet.CHARACTER.at(4, 3),
            SpriteSheet.CHARACTER.at(4, 4)
    );

    /**
     * Animation for the character walking right.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(2, 1),
            SpriteSheet.CHARACTER.at(2, 2),
            SpriteSheet.CHARACTER.at(2, 3),
            SpriteSheet.CHARACTER.at(2, 4)
    );

    /**
     * Animation for the character idle state.
     */
    public static final Animation<TextureRegion> CHARACTER_IDLE = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 3)
    );

    /**
     * Animation for the enemy walking (e.g., Grinch enemy).
     */
    public static final Animation<TextureRegion> ENEMY_WALK_DOWN = new Animation<>(0.2f,
            SpriteSheet.ENEMIES.at(5, 1),
            SpriteSheet.ENEMIES.at(5, 2),
            SpriteSheet.ENEMIES.at(5, 3)
    );


    public static final Animation<TextureRegion> BOMB = new Animation<>(0.1f,
            SpriteSheet.ORIGINALBOMBERMAN.at(4, 3),
            SpriteSheet.ORIGINALBOMBERMAN.at(4, 3)
    );

    public static final Animation<TextureRegion> EXPLOSION = new Animation<>(0.1f,
            SpriteSheet.EXPLOSION.at(1, 1),
            SpriteSheet.EXPLOSION.at(1, 2),
            SpriteSheet.EXPLOSION.at(1, 3),
            SpriteSheet.EXPLOSION.at(1, 4)
    );



}
