package m8.uf3.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import m8.uf3.MainGame;
import m8.uf3.helpers.GestorAssetsJoc;
import m8.uf3.utils.Configuracio;

public class GameOver implements Screen {
    private MainGame game;
    private OrthographicCamera camera;
    private int puntuacion;

    public GameOver(MainGame game, int puntuacion) {
        this.game = game;
        this.puntuacion = puntuacion;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        GestorAssetsJoc.font.getData().setScale(4f);
        GestorAssetsJoc.font.draw(game.getBatch(), "GAME OVER",
            Configuracio.AMPLADA_JO/2 - 300,
            Configuracio.ALTURA_JO/2 + 100);

        GestorAssetsJoc.font.getData().setScale(2f);
        GestorAssetsJoc.font.draw(game.getBatch(), "Puntuacio: " + puntuacion,
            Configuracio.AMPLADA_JO/2 - 200,
            Configuracio.ALTURA_JO/2);

        GestorAssetsJoc.font.getData().setScale(1.5f);
        GestorAssetsJoc.font.draw(game.getBatch(), "Prem per jugar de nou",
            Configuracio.AMPLADA_JO/2 - 210,
            Configuracio.ALTURA_JO/2 - 100);
        game.getBatch().end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game)); // Transición limpia
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = Configuracio.AMPLADA_JO;
        camera.viewportHeight = Configuracio.ALTURA_JO;
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Liberar recursos específicos si los hay
    }
}
