package m8.uf3.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import m8.uf3.MainGame;
import m8.uf3.utils.Configuracio;

public class MenuScreen implements Screen {
    private MainGame game;
    private OrthographicCamera camera;
    private Texture fondo;

    public MenuScreen(MainGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO);
        fondo = new Texture("18 may 2025, 21_19_32.png"); // Cargar la imagen
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        // Dibujar la imagen de fondo
        game.getBatch().draw(fondo, 0, 0, Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO);

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
    public void dispose() {
        fondo.dispose(); // Liberar la textura
    }
}
