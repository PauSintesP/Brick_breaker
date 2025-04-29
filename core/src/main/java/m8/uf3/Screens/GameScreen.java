package m8.uf3.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import m8.uf3.actors.Barra;
import m8.uf3.actors.Pilota;
import m8.uf3.actors.Block;
import m8.uf3.utils.Configuracio;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private Stage stage;
    private OrthographicCamera camera;
    private Barra barra;
    private Pilota pilota;
    private List<Block> blocks;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO, camera));

        // Crear barra
        barra = new Barra(Configuracio.AMPLADA_JO / 2 - Configuracio.AMPLADA_BARRA / 2, 50);
        stage.addActor(barra);

        // Crear pilota
        pilota = new Pilota(Configuracio.AMPLADA_JO / 2, 100, 16);
        stage.addActor(pilota);

        // Crear bloques
        blocks = new ArrayList<>();
        crearBloques();
    }

    private void crearBloques() {
        float blockWidth = 64;
        float blockHeight = 32;
        int rows = 5;
        int cols = 7;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float x = col * (blockWidth + 5) + 10;
                float y = Configuracio.ALTURA_JO - (row * (blockHeight + 5)) - 50;
                Block block = new Block(x, y, blockWidth, blockHeight, row % 4);
                blocks.add(block);
                stage.addActor(block);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar lógica del juego
        actualizar(delta);

        // Dibujar escena
        stage.act(delta);
        stage.draw();
    }

    private void actualizar(float delta) {
        // Movimiento de la barra
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX() * (Configuracio.AMPLADA_JO / Gdx.graphics.getWidth());
            barra.setX(touchX - barra.getWidth() / 2);
        }

        // Colisiones de la pelota con la barra
        if (pilota.getBounds().overlaps(barra.getLimits())) {
            pilota.invertirY();
        }

        // Colisiones de la pelota con los bloques
        for (Block block : blocks) {
            if (!block.estaDestruit() && pilota.getBounds().overlaps(block.getLimits())) {
                block.repColisio();
                pilota.invertirY();
                break;
            }
        }

        // Reiniciar si la pelota sale de los límites
        if (pilota.estaForaDeLimits()) {
            reiniciarJuego();
        }
    }

    private void reiniciarJuego() {
        pilota.setPosition(Configuracio.AMPLADA_JO / 2, 100);
        pilota.llança(0, 0);
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
