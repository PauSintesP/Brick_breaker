package m8.uf3.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GestorAssetsJoc {
    // Textures principals
    public static Texture fullSprites;
    public static Texture fons;

    // Elements del joc
    public static TextureRegion texturaPilota;
    public static TextureRegion texturaBarra;
    public static TextureRegion[] texturaLladres;

    // Efectes de so
    public static Sound soColisio;
    public static Sound soDestruccio;

    public static void carregar() {
        // Carregar fitxers principals
        fullSprites = new Texture(Gdx.files.internal("paddles_and_balls.png"));
        fons = new Texture(Gdx.files.internal("Backround_Tiles.png"));

        // Configurar elements del joc
        texturaPilota = new TextureRegion(fullSprites, 0, 0, 32, 32);
        texturaBarra = new TextureRegion(fullSprites, 35, 0, 128, 32);

        // Configurar array de lladres
        Texture texturaLladreBase = new Texture(Gdx.files.internal("bricks.png"));
        texturaLladres = new TextureRegion[4];
        for(int i = 0; i < 4; i++) {
            texturaLladres[i] = new TextureRegion(texturaLladreBase, i * 64, 0, 64, 32);
        }

        // Carregar efectes de so
        soColisio = Gdx.audio.newSound(Gdx.files.internal("sounds/blip.wav"));
        soDestruccio = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        // Configurar filtres
        fullSprites.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fons.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public static void descarregar() {
        fullSprites.dispose();
        fons.dispose();
        soColisio.dispose();
        soDestruccio.dispose();
    }
}
