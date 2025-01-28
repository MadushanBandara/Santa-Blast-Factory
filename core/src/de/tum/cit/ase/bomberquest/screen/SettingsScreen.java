package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;

public class SettingsScreen implements Screen {

    private final Stage stage;
    private final BomberQuestGame game;

    public SettingsScreen(BomberQuestGame game) {
        this.game = game;

        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Mute Button
        TextButton muteButton = new TextButton("Mute", game.getSkin());
        table.add(muteButton).width(300).padBottom(10).row();
        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicTrack.CLICKSOUND.play(false);
                BomberQuestGame.muteMusic();
            }
        });

        // Unmute Button
        TextButton unmuteButton = new TextButton("Unmute", game.getSkin());
        table.add(unmuteButton).width(300).padBottom(10).row();
        unmuteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicTrack.CLICKSOUND.play(false);
                BomberQuestGame.setMuted(false);
                MusicTrack.GAMEMENUMUSIC.play(true);
            }
        });

        // Back Button
        TextButton backButton = new TextButton("Back", game.getSkin());
        table.add(backButton).width(300).padBottom(10).row();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicTrack.CLICKSOUND.play(false);
                game.goToMenu(); // Navigate back to the menu screen
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f); // Cap frame time to 250ms to prevent spiral of death        ScreenUtils.clear(Color.BLACK);
        ScreenUtils.clear(Color.BLACK);
        stage.act(frameTime); // Update the stage
        stage.draw(); // Draw the stage
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
