package de.tum.cit.ase.bomberquest.map;

import de.tum.cit.ase.bomberquest.Actors.Bomb;
import de.tum.cit.ase.bomberquest.Actors.Santa;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.screen.Hud;
//import de.tum.cit.ase.bomberquest.map.Exit;//


public class GameStatus {

   private static boolean victory=false;
   private static boolean gameOver=false;



    public static boolean GameWon() {
        if(Player.isAlive() && GameMap.getEnemiesGenerated()==0 && Santa.isSaved() && Tile.isExitFound()){
            setVictory(true);
            Player.PlayerWon();

            System.out.println("Congratulations! You have won the game!");
        }
        return victory;

    }

    public static boolean GameOver(){
        if(!Player.isAlive() || Hud.isTimeUp() || Bomb.getMaxBombs()==0 && GameMap.getEnemiesGenerated()>0 ||  Bomb.getMaxBombs()==0 && !Tile.isExitRevealed() || Bomb.getMaxBombs()==0 && !Santa.isSaved()  ){
            setGameOver(true);
        }
        return gameOver;
    }



    public static void reset() {
        setGameOver(false);
        setVictory(false);
    }

    public static boolean isVictory() {
        return victory;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setVictory(boolean victory) {
        GameStatus.victory = victory;
    }

    public static void setGameOver(boolean gameOver) {
        GameStatus.gameOver = gameOver;
    }
}
