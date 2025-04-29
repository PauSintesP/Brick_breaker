package m8.uf3.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import m8.uf3.helpers.GestorAssetsJoc;
import m8.uf3.utils.Configuracio;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Pilota extends Actor {
    private Vector2 velocitat;
    private float radi;
    private boolean estaLlançada;

    public Pilota(float x, float y, float radi) {
        setPosition(x, y);
        this.radi = radi;
        this.velocitat = new Vector2(0, 0);
        this.estaLlançada = false;
        setBounds(x, y, radi*2, radi*2);
    }

    public void llança(float velocitatX, float velocitatY) {
        if(!estaLlançada) {
            this.velocitat.set(velocitatX, velocitatY);
            estaLlançada = true;
        }
    }

    @Override
    public void act(float delta) {
        if(estaLlançada) {
            setX(getX() + velocitat.x * delta);
            setY(getY() + velocitat.y * delta);
            comprovaColisionsAmbParets();
        }
    }

    private void comprovaColisionsAmbParets() {
        if(getX() < 0 || getX() + getWidth() > Configuracio.AMPLADA_JO) {
            velocitat.x *= -1;
        }
        if(getY() + getHeight() > Configuracio.ALTURA_JO) {
            velocitat.y *= -1;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(GestorAssetsJoc.texturaPilota, getX(), getY(), radi*2, radi*2);
    }

    public Vector2 getVelocitat() { return velocitat; }
    public void invertirY() { velocitat.y *= -1; }
    public void invertirX() { velocitat.x *= -1; }
    public boolean estaForaDeLimits() { return getY() + getHeight() < 0; }
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
