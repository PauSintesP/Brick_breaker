package m8.uf3.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.math.MathUtils;
import m8.uf3.actors.Barra;
import m8.uf3.actors.Pilota;
import m8.uf3.actors.Block;
import m8.uf3.helpers.GestorAssetsJoc;
import m8.uf3.utils.Configuracio;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GameScreen implements Screen {
    private Stage stage;
    private OrthographicCamera camera;
    private Barra barra;
    private Pilota pilota;
    private List<Block> blocks;
    private boolean gameOver;
    private boolean allBlocksDestroyed;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        stage = new Stage(new FillViewport(Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO, camera));
        Gdx.input.setInputProcessor(stage);

        // Crear barra
        barra = new Barra(Configuracio.AMPLADA_JO / 2 - Configuracio.AMPLADA_BARRA / 2, 50);
        stage.addActor(barra);

        // Crear pilota
        pilota = new Pilota(Configuracio.AMPLADA_JO / 2, 100, 16);
        stage.addActor(pilota);

        // Crear bloques
        blocks = new ArrayList<>();
        crearBloques();

        gameOver = false;
        allBlocksDestroyed = false;
    }

    private void crearBloques() {
        float blockWidth = 64;
        float blockHeight = 32;
        int rows = 5;
        int cols = (int)(Configuracio.AMPLADA_JO / (blockWidth + 5)) - 2; // Ajustar automáticamente las columnas

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

        // Dibujar el fondo
        stage.getBatch().begin();
        stage.getBatch().draw(GestorAssetsJoc.fons, 0, 0, Configuracio.AMPLADA_JO, Configuracio.ALTURA_JO);
        stage.getBatch().end();

        // Lanzar la pelota con toque
        if (!pilota.estaLlançada() && Gdx.input.justTouched()) {
            // Velocidad inicial con ángulo aleatorio
            float angle = MathUtils.random(60, 120);
            float velocidadX = 400 * MathUtils.cosDeg(angle);
            float velocidadY = 400 * MathUtils.sinDeg(angle);
            pilota.llança(velocidadX, velocidadY);
        }

        // Actualizar lógica del juego
        if (!gameOver && !allBlocksDestroyed) {
            actualizar(delta);
        }

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

        // Colisiones de la pelota con la barra - Mejorada
        if (pilota.getBounds().overlaps(barra.getLimits())) {
            // Solo invertir si viene desde arriba (velocidad Y negativa)
            if (pilota.getVelocitat().y < 0) {
                // Calcular el punto de impacto para cambiar dirección
                float relativeIntersectX = (barra.getX() + (barra.getWidth() / 2)) -
                    (pilota.getX() + pilota.getWidth() / 2);
                float normalizedRelativeIntersection = relativeIntersectX / (barra.getWidth() / 2);

                // Ángulo de rebote basado en dónde golpeó la barra (máximo 75 grados)
                float bounceAngle = normalizedRelativeIntersection * 75;

                // Velocidad constante de 400 píxeles/segundo
                float speed = 400;
                pilota.getVelocitat().x = speed * MathUtils.sinDeg(bounceAngle);
                pilota.getVelocitat().y = speed * MathUtils.cosDeg(bounceAngle);

                try {
                    GestorAssetsJoc.soColisio.play(0.5f);
                } catch (Exception e) {
                    Gdx.app.log("GameScreen", "Error reproduciendo sonido de colisión");
                }
            }
        }

        // Colisiones de la pelota con los bloques
        Iterator<Block> iter = blocks.iterator();
        boolean colisionDetectada = false;

        while (iter.hasNext() && !colisionDetectada) {
            Block block = iter.next();
            if (!block.estaDestruit() && pilota.getBounds().overlaps(block.getLimits())) {
                // Determinar dirección de rebote
                float overlapLeft = pilota.getX() + pilota.getWidth() - block.getX();
                float overlapRight = block.getX() + block.getWidth() - pilota.getX();
                float overlapTop = pilota.getY() + pilota.getHeight() - block.getY();
                float overlapBottom = block.getY() + block.getHeight() - pilota.getY();

                // Encontrar la menor superposición para determinar dirección de rebote
                float minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                    Math.min(overlapTop, overlapBottom));

                if (minOverlap == overlapLeft || minOverlap == overlapRight) {
                    pilota.invertirX();
                } else {
                    pilota.invertirY();
                }

                block.repColisio();
                colisionDetectada = true;

                try {
                    GestorAssetsJoc.soDestruccio.play(0.5f);
                } catch (Exception e) {
                    Gdx.app.log("GameScreen", "Error reproduciendo sonido de destrucción");
                }
            }
        }

        // Verificar si todos los bloques han sido destruidos
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

        // Reiniciar si la pelota sale de los límites inferiores
        if (pilota.estaForaDeLimits()) {
            gameOver = true;
            reiniciarJuego();
        }
    }

    private void reiniciarJuego() {
        // Pequeña pausa antes de reiniciar
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Gdx.app.log("GameScreen", "Error en pausa de reinicio");
        }

        // Eliminar la pelota actual del escenario
        pilota.remove();

        // Crear una nueva pelota
        pilota = new Pilota(Configuracio.AMPLADA_JO / 2, 100, 16);
        stage.addActor(pilota);

        // Reiniciar bloques si todos fueron destruidos
        if (allBlocksDestroyed) {
            for (Block block : blocks) {
                block.remove();
            }
            blocks.clear();
            crearBloques();
        }

        gameOver = false;
        allBlocksDestroyed = false;
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
