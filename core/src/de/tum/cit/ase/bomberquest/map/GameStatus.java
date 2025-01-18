package de.tum.cit.ase.bomberquest.map;

import de.tum.cit.ase.bomberquest.Actors.Santa;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.screen.Hud;
//import de.tum.cit.ase.bomberquest.map.Exit;//


public class GameStatus {

   private static boolean Victory;
   private static boolean GameOver;



    public static boolean GameWon() {
        if (!Player.isAlive() || Hud.isTimeUp()) {
            System.out.println("Game Over! You lose.");
            setGameOver(true);
            return false;
        }
        else if (Player.isAlive() && GameMap.getEnemiesGenerated() == 0 && Santa.isSaved() && Tile.isExitFound()) {
            System.out.println("Congratulations! You have won the game!");
            setVictory(true);
            return true;
        }
        else return false;
    }

    public static boolean isVictory() {
        return Victory;
    }

    public static boolean isGameOver() {
        return GameOver;
    }

    public static void setVictory(boolean victory) {
        Victory = victory;
    }

    public static void setGameOver(boolean gameOver) {
        GameOver = gameOver;
    }

    public static void reset() {
        setGameOver(false);
        setVictory(false);
    }
}
