package m8.uf3.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.math.MathUtils;
import m8.uf3.MainGame;
import m8.uf3.actors.Barra;
import m8.uf3.actors.Pilota;
import m8.uf3.actors.Block;
import m8.uf3.helpers.GestorAssetsJoc;
import m8.uf3.utils.Configuracio;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GameScreen implements Screen {
    private MainGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private Barra barra;
    private Pilota pilota;
    private List<Block> blocks;
    private boolean gameOver;
    private boolean allBlocksDestroyed;
    private int score;
    private int vidas;

    public GameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        stage = new Stage(new FillViewport(Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO, camera));
        Gdx.input.setInputProcessor(stage);
        barra = new Barra(Configuracio.AMPLADA_JO / 2 - Configuracio.AMPLADA_BARRA / 2, Configuracio.ALTURA_BARRA);
        stage.addActor(barra);
        pilota = new Pilota(Configuracio.AMPLADA_JO / 2, 100, 16);
        stage.addActor(pilota);
        blocks = new ArrayList<>();
        crearBloques();
        gameOver = false;
        allBlocksDestroyed = false;
        score = 0;
        vidas = 3;
    }

    private void crearBloques() {
        float blockWidth = 64;
        float blockHeight = 32;
        int rows = 5;
        int cols = (int)(Configuracio.AMPLADA_JO / (blockWidth + 5)) - 2;
        float startX = (Configuracio.AMPLADA_JO - (cols * (blockWidth + 5) - 5)) / 2;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float x = startX + col * (blockWidth + 5);
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

        stage.act(delta);

        stage.getBatch().begin();
        stage.getBatch().draw(GestorAssetsJoc.fons, 0, 0, Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO);
        stage.getBatch().end();

        stage.draw();

        game.getBatch().begin();
        GestorAssetsJoc.font.getData().setScale(1f);
        GestorAssetsJoc.font.draw(game.getBatch(), "Punts: " + score, Configuracio.X_PUNTS_LETRERO, Configuracio.Y_PUNTS_LETRERO - 20);
        GestorAssetsJoc.font.draw(game.getBatch(), "Vides: " + vidas, Configuracio.X_PUNTS_LETRERO, Configuracio.Y_PUNTS_LETRERO - 50);
        game.getBatch().end();

        if (!pilota.estaLlançada() && Gdx.input.justTouched()) {
            float angle = MathUtils.random(60, 120);
            float velocidadX = 400 * MathUtils.cosDeg(angle);
            float velocidadY = 400 * MathUtils.sinDeg(angle);
            pilota.llança(velocidadX, velocidadY);
        }

        if (!gameOver && !allBlocksDestroyed) {
            actualizar(delta);
        }

        if (allBlocksDestroyed) {
            for (Block block : blocks) {
                block.remove();
            }
            blocks.clear();
            crearBloques();
            allBlocksDestroyed = false;
            pilota.llança(pilota.getVelocitat().x, Math.abs(pilota.getVelocitat().y));
        }
    }

    private void actualizar(float delta) {
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX() * (Configuracio.AMPLADA_JO / Gdx.graphics.getWidth());
            barra.setX(touchX - barra.getWidth() / 2);
        }

        if (pilota.getBounds().overlaps(barra.getLimits())) {
            if (pilota.getVelocitat().y < 0) {
                float relativeIntersectX = (barra.getX() + (barra.getWidth() / 2)) - (pilota.getX() + pilota.getWidth() / 2);
                float normalizedRelativeIntersection = relativeIntersectX / (barra.getWidth() / 2);
                float bounceAngle = normalizedRelativeIntersection * 75;
                float speed = 400;
                pilota.getVelocitat().x = speed * MathUtils.sinDeg(bounceAngle);
                pilota.getVelocitat().y = speed * MathUtils.cosDeg(bounceAngle);
            }
        }

        Iterator<Block> iter = blocks.iterator();
        boolean colisionDetectada = false;
        while (iter.hasNext() && !colisionDetectada) {
            Block block = iter.next();
            if (!block.estaDestruit() && pilota.getBounds().overlaps(block.getLimits())) {
                float overlapLeft = pilota.getX() + pilota.getWidth() - block.getX();
                float overlapRight = block.getX() + block.getWidth() - pilota.getX();
                float overlapTop = pilota.getY() + pilota.getHeight() - block.getY();
                float overlapBottom = block.getY() + block.getHeight() - pilota.getY();
                float minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));
                if (minOverlap == overlapLeft || minOverlap == overlapRight) {
                    pilota.invertirX();
                } else {
                    pilota.invertirY();
                }
                block.repColisio();
                colisionDetectada = true;
                if (block.estaDestruit()) score += 100;
            }
        }

        boolean todosDestruidos = true;
        for (Block block : blocks) {
            if (!block.estaDestruit()) {
                todosDestruidos = false;
                break;
            }
        }
        if (todosDestruidos) {
            allBlocksDestroyed = true;
        }

        if (pilota.estaForaDeLimits()) {
            vidas--;
            if (vidas <= 0) {
                game.setScreen(new GameOver(game, score));
                dispose();
            } else {
                gameOver = true;
                reiniciarJuego();
            }
        }
    }

    private void reiniciarJuego() {
        pilota.remove();
        pilota = new Pilota(Configuracio.AMPLADA_JO / 2, 100, 16);
        stage.addActor(pilota);
        gameOver = false;
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
