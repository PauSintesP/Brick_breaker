package m8.uf3.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;
import m8.uf3.helpers.GestorAssetsJoc;
import m8.uf3.utils.Configuracio;

public class Barra extends Actor {
    private float velocitat;
    private Rectangle limits;

    public Barra(float x, float y) {
        setPosition(x, y);
        this.velocitat = Configuracio.VELOCITAT_BARRA;
        this.limits = new Rectangle(x, y, Configuracio.AMPLADA_BARRA, Configuracio.ALTURA_BARRA);
        setBounds(x, y, Configuracio.AMPLADA_BARRA, Configuracio.ALTURA_BARRA);
    }

    public void mouEsquerra(float delta) {
        setX(Math.max(0, getX() - velocitat * delta));
    }

    public void mouDreta(float delta) {
        setX(Math.min(Configuracio.AMPLADA_JO - getWidth(), getX() + velocitat * delta));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        limits.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(GestorAssetsJoc.texturaBarra, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getLimits() { return limits; }
}
