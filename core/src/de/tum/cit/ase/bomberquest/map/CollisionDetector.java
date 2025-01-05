package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.Actors.Player;

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

        if ((UserA instanceof Player && UserB instanceof Enemy) || (UserA instanceof Enemy && UserB instanceof Player)) {
            if (UserA instanceof Player) {
                ((Player) UserA).PlayerDied();
            } else {
                ((Player) UserB).PlayerDied();
            }
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
