package m8.uf3.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import m8.uf3.MainGame;
import m8.uf3.utils.Configuracio;

public class MenuScreen implements Screen {
    private MainGame game;
    private OrthographicCamera camera;

    public MenuScreen(MainGame game) {
        this.game = game;
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
        game.getFont().getData().setScale(3f);
        game.getFont().setColor(1, 1, 1, 1);
        game.getFont().draw(game.getBatch(), "Toca para empezar",
            Configuracio.AMPLADA_JO/2 - 150,
            Configuracio.ALTURA_JO/2);
        game.getBatch().end();
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
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
    public void dispose() {}
}
