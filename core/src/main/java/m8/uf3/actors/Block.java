package m8.uf3.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;
import m8.uf3.helpers.GestorAssetsJoc;

public class Block extends Actor {
    private boolean destruit;
    private Rectangle limits;
    private int puntsDeVida;
    private int tipusTextura;

    public Block(float x, float y, float amplada, float altura, int tipus) {
        setPosition(x, y);
        setSize(amplada, altura);
        this.limits = new Rectangle(x, y, amplada, altura);
        this.destruit = false;
        this.puntsDeVida = 1;
        this.tipusTextura = tipus % GestorAssetsJoc.texturaLladres.length;
    }

    public void repColisio() {
        puntsDeVida--;
        if(puntsDeVida <= 0) destruit = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!destruit) {
            batch.draw(GestorAssetsJoc.texturaLladres[tipusTextura],
                getX(), getY(), getWidth(), getHeight());
        }
    }

    public Rectangle getLimits() { return limits; }
    public boolean estaDestruit() { return destruit; }
}
