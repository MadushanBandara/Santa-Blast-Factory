package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import de.tum.cit.ase.bomberquest.BomberQuestGame;

/**
 * This enum is used to manage the music tracks in the game.
 * Using an enum for this purpose is a good practice, as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 * See the assets/audio folder for the actual music files.
 * Feel free to add your own music tracks and use them in the game!
 */
public enum MusicTrack {

    BACKGROUND("DecktheHalls.mp3", 0.2f, true),
    EXPLOSION("explosion.mp3", 0.2f, false),
    GAMEOVER("game_over.mp3", 0.02f, false),
    GAMEOVERMUSIC("sadgameover.mp3",0.5f,false),
    GAMEMENUMUSIC("wintery loop.wav",0.1f,false),
    GAMEVICTORYMUSIC("JingleBells.mp3",0.2f,false),
    COLLECTING("completetask_0.mp3",2f,false),
    HOHOHO("hohoho.mp3",0.1f,false),
    BOMBDROPSOUND("appear-online.mp3", 0.3f,false),

    WIN("win.mp3", 0.2f,false),
    CLICKSOUND("click_sound_5.mp3",1.5f,false);



    /** The music file owned by this variant. */
    private final Music music;



    MusicTrack(String fileName, float volume, boolean loop) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + fileName));
        this.music.setLooping(loop);
        this.music.setVolume(volume);
    }



    /**
     * Play this music track.
     * This will not stop other music from playing - if you add more tracks, you will have to handle that yourself.
     */
    public void play(boolean loop) {
        if(!BomberQuestGame.isMuted()){
            this.music.play();
            this.music.setLooping(loop);
        }

    }

    public void stopMusic(){
        this.music.stop();
    }




    public Music getMusic() {
        return music;
    }



}
