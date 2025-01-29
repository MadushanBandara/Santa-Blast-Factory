package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.Actors.Enemy;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.Actors.Santa;



//https://stackoverflow.com/questions/7447811/clean-way-to-detect-collision-of-object-with-box2d?rq=3
//https://libgdx.com/wiki/extensions/physics/box2d

public class CollisionDetector implements ContactListener {

    private GameMap map;

    public CollisionDetector(GameMap map) {
        this.map = map;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture ActorA = contact.getFixtureA();
        Fixture ActorB = contact.getFixtureB();
        Object UserA = ActorA.getBody().getUserData();
        Object UserB = ActorB.getBody().getUserData();

        // Player and Enemy interaction: Enemy kill player logic
        if ((UserA instanceof Player && UserB instanceof Enemy) || (UserA instanceof Enemy && UserB instanceof Player)) {
            Player player = (UserA instanceof Player) ? (Player) UserA : (Player) UserB;
             {
                player.PlayerDied(); // Player dies if no extra lives
                GameMap.updateLifeCounter();//render lives counter
            }
        }

        //enemy and enemy collision: an enemy is made to be changing direction when it collides with another enemy

        if (UserA instanceof Enemy && UserB instanceof Enemy) {
            Enemy enemy1 = (Enemy) UserA;
            Enemy enemy2 = (Enemy) UserB;
            enemy1.changeDirection(); // make both Change direction as they may be stuck in a corner which happened often
            enemy2.changeDirection();
        }

        // enemy collision with destructible tiles and indestructible tiles:an enemy is made to be changing direction when it collides with a tile
        if (UserA instanceof Enemy && UserB instanceof Tile) {
            Tile tile = (Tile) UserB;
            if (tile.getTileType() == 1 || tile.getTileType() == 0) {
                ((Enemy) UserA).changeDirection();
            }
        } else if (UserA instanceof Tile && UserB instanceof Enemy) {
            Tile tile = (Tile) UserA;
            if (tile.getTileType() == 1 || tile.getTileType() == 0) {
                ((Enemy) UserB).changeDirection();
            }
        }


        // Player and Santa interaction, Saving Santa Scheme
        if ((UserA instanceof Player && UserB instanceof Santa) || (UserA instanceof Santa && UserB instanceof Player)) {
            Santa santa = (UserA instanceof Santa) ? (Santa) UserA : (Santa) UserB;
            if(!santa.isSaved()) {
                santa.SantaSaved();
            }
        }


        //Player and Exit interaction: Player finds Exit tile (collide)
        if ((UserA instanceof Player && UserB instanceof Tile && ((Tile) UserB).getTileType() == 8) ||
                (UserA instanceof Tile && ((Tile) UserA).getTileType() == 8 && UserB instanceof Player)) {
            Tile.setExitFound(true);
            System.out.println("you can leave from here");
        } else {
            Tile.setExitFound(false); //Exit is only useful when player is above it.
        }

        // Player and Power-Up Tile interaction
        if ((UserA instanceof Player && UserB instanceof Tile && ((Tile) UserB).getTileType() == 9) ||
                (UserA instanceof Tile && ((Tile) UserA).getTileType() == 9 && UserB instanceof Player)) {
            Tile tile = (UserA instanceof Tile) ? (Tile) UserA : (Tile) UserB;
            Player player = (UserA instanceof Player) ? (Player) UserA : (Player) UserB;
            System.out.println("you found a powerup");

            if (!tile.isPowerupRedeemed()) {//ensure only redeemed once
                tile.setPowerupFound(true);
                tile.grantPowerup(player);//get powerup
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
