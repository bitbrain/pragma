package de.bitbrain.pragma.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.AbstractRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.ai.DevilController;
import de.bitbrain.pragma.core.LevelLoader;
import de.bitbrain.pragma.events.SayEvent;
import de.bitbrain.pragma.graphics.CharacterInitializer;
import de.bitbrain.pragma.ui.SpeechHandler;


public class IngameScreen extends AbstractScreen<BrainGdxGame> {

    private LevelLoader loader;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        loader = new LevelLoader(context);
        setBackgroundColor(Colors.BACKGROUND);
        context.getScreenTransitions().in(3.5f);
        SharedAssetManager.getInstance().load(Assets.TiledMaps.INTRO, TiledMap.class);
        SharedAssetManager.getInstance().finishLoading();
        TiledMap map = SharedAssetManager.getInstance().get(Assets.TiledMaps.INTRO, TiledMap.class);
        context.getTiledMapManager().load(map, context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);

        final Texture texture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER_TILESET);
        SpriteSheet sheet = new SpriteSheet(texture, 4, 8);
        CharacterInitializer.createAnimations(context, sheet);

        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setLutIntensity(0.7f);
        vignette.setIntensity(0.7f);
        vignette.setSaturationMul(1.1f);
        context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(vignette);

        // CORE components
        context.getEventManager().register(new SpeechHandler(context.getStage()), SayEvent.class);
        loader.load(Assets.TiledMaps.INTRO);

        // Creepy stuff
        Music soundscape = SharedAssetManager.getInstance().get(Assets.Musics.SOUNDSCAPE, Music.class);
        soundscape.setLooping(true);
        soundscape.setVolume(0.1f);
        soundscape.play();
    }

    @Override
    protected void onUpdate(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        loader.update();
    }
}
