package m8.uf3.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GestorAssetsJoc {
    public static Texture fullSprites;
    public static Texture fons;
    public static TextureRegion texturaPilota;
    public static TextureRegion texturaBarra;
    public static TextureRegion[] texturaLladres;
    public static BitmapFont font;

    public static void carregar() {
        fullSprites = new Texture(Gdx.files.internal("paddles_and_balls.png"));
        fons = new Texture(Gdx.files.internal("FondoFinal.png"));
        texturaPilota = new TextureRegion(fullSprites, 175, 50, 16, 16);
        texturaBarra = new TextureRegion(fullSprites, 35, 70, 64, 16);

        Texture texturaLladreBase = new Texture(Gdx.files.internal("bricks.png"));
        texturaLladres = new TextureRegion[4];
        for(int i = 0; i < 4; i++) {
            texturaLladres[i] = new TextureRegion(texturaLladreBase, i * 64, 0, 64, 32);
        }

        fullSprites.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fons.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), false);
    }

    public static void descarregar() {
        fullSprites.dispose();
        fons.dispose();
        font.dispose();
    }
}
