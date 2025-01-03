package de.tum.cit.ase.bomberquest.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.Actors.Bomb;

public class KeyHandler {

    private final GameMap handlemap;

    public KeyHandler(GameMap handlemap) {
        this.handlemap = handlemap;
    }

    public void keyHandler() {
        Player player = handlemap.getPlayer(); // Access the player from the map

        player.stopMovement();

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.moveUp();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.moveDown();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        }
    }
}
