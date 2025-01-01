package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class IndestructibleTile extends Tile {

    private final World world;
    private Body body;


    public IndestructibleTile(World world, int x, int y, int tileType) {
        super(x, y, INDESTRUCTIBLE_WALL);
        this.world = world;
    }
}
