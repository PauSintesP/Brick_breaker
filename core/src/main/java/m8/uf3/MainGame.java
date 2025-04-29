package m8.uf3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;

import m8.uf3.helpers.GestorAssetsJoc;
import m8.uf3.Screens.GameScreen;

public class MainGame extends Game {
    @Override
    public void create() {
        GestorAssetsJoc.carregar();
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        GestorAssetsJoc.descarregar();
    }

    public Batch getBatch() {
    }
}
