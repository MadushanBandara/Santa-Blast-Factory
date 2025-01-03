package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.physics.box2d.*;

public class IndestructibleTile extends Tile {

    private Body body;



    public IndestructibleTile(World world, int x, int y, int tileType) {
        super(world, x, y, INDESTRUCTIBLE_WALL);
    }



}
