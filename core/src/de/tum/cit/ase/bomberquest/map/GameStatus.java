package de.tum.cit.ase.bomberquest.map;

import de.tum.cit.ase.bomberquest.Actors.Santa;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.screen.Hud;
//import de.tum.cit.ase.bomberquest.map.Exit;//


public class GameStatus {

    public static boolean GameWon() {

        boolean victory = false;
        if(Player.isAlive() && GameMap.getEnemiesGenerated()==0 && Santa.isSaved() && Tile.isExitFound()){
            victory=true;
            System.out.println("Congratulations! You have won the game!");
        } else{
            victory=false;
        }
        return victory;

    }

    public static boolean GameOver(){
        boolean GameOver=false;
        if(!Player.isAlive()){
            GameOver=true;
            System.out.println("Game Over!, you lose");

        }
        else if(Hud.isTimeUp()){
            GameOver=true;
            System.out.println("Game Over!, you lose");
        }
        return GameOver;
    }

}
