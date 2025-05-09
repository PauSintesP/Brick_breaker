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
        fons = new Texture(Gdx.files.internal("FondoFinal.png"));

        // Configurar elements del joc
        texturaPilota = new TextureRegion(fullSprites, 175, 50, 16, 16);
        texturaBarra = new TextureRegion(fullSprites, 35, 70, 64, 16);

        // Configurar array de lladres
        Texture texturaLladreBase = new Texture(Gdx.files.internal("bricks.png"));
        texturaLladres = new TextureRegion[4];
        for(int i = 0; i < 4; i++) {
            texturaLladres[i] = new TextureRegion(texturaLladreBase, i * 64, 0, 64, 32);
        }

        // Carregar efectes de so con manejo de errores
        try {
            soColisio = Gdx.audio.newSound(Gdx.files.internal("sounds/blip.wav"));
            soDestruccio = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        } catch (Exception e) {
            Gdx.app.log("GestorAssetsJoc", "No se pudieron cargar los archivos de sonido: " + e.getMessage());
            // Crear sonidos vacíos para evitar NullPointerException
            soColisio = Gdx.audio.newSound(Gdx.files.internal("paddles_and_balls.png"));
            soDestruccio = Gdx.audio.newSound(Gdx.files.internal("paddles_and_balls.png"));
        }

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
