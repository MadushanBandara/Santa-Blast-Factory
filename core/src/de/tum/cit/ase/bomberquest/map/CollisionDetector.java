package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.Actors.Santa;
//import de.tum.cit.ase.bomberquest.map.Chest;//


//https://stackoverflow.com/questions/7447811/clean-way-to-detect-collision-of-object-with-box2d?rq=3
//https://libgdx.com/wiki/extensions/physics/box2d

public class CollisionDetector implements ContactListener {

//ChatGpt help here
    @Override
    public void beginContact(Contact contact) {
        Fixture ActorA= contact.getFixtureA();
        Fixture ActorB= contact.getFixtureB();
        Object  UserA=ActorA.getBody().getUserData();
        Object  UserB=ActorB.getBody().getUserData();

        // Player and Enemy interaction
        if ((UserA instanceof Player && UserB instanceof Enemy) || (UserA instanceof Enemy && UserB instanceof Player)) {
            Player player = (UserA instanceof Player) ? (Player) UserA : (Player) UserB;
            player.PlayerDied();
        }

        // Player and Santa interaction
        if ((UserA instanceof Player && UserB instanceof Santa) || (UserA instanceof Santa && UserB instanceof Player)) {
            Santa santa = (UserA instanceof Santa) ? (Santa) UserA : (Santa) UserB;
            santa.SantaSaved();
        }

        // Player and Exit interaction
        if ((UserA instanceof Player && UserB instanceof Exit) || (UserA instanceof Exit && UserB instanceof Player)) {
            Exit exit = (UserA instanceof Exit) ? (Exit) UserA : (Exit) UserB;
            exit.exitFound();
        }

        // Player and Power-Up Tile interaction
        if ((UserA instanceof Player && UserB instanceof Tile && ((Tile) UserB).getTileType() == Tile.POWERUP) ||
                (UserA instanceof Tile && ((Tile) UserA).getTileType() == Tile.POWERUP && UserB instanceof Player)) {
            Player player = (UserA instanceof Player) ? (Player) UserA : (Player) UserB;
            player.PlayerGrantedPowerUP();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }
}
