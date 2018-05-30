package de.bitbrain.pragma.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.Config;
import de.bitbrain.pragma.core.CameraController;
import de.bitbrain.pragma.core.CharacterType;
import de.bitbrain.pragma.core.LevelLoader;
import de.bitbrain.pragma.events.SayEvent;
import de.bitbrain.pragma.graphics.CharacterInitializer;
import de.bitbrain.pragma.graphics.ScreenShake;
import de.bitbrain.pragma.ui.SpeechHandler;


public class IngameScreen extends AbstractScreen<BrainGdxGame> {

    private LevelLoader loader;

    private ScreenShake screenShake;

    private CameraController cameraController;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(final GameContext context) {
        loader = new LevelLoader(context, getGame());
        setBackgroundColor(Colors.BACKGROUND);
        context.getScreenTransitions().in(3.5f);

        // debug events
        if (Config.DEBUG) {
            Texture texture = GraphicsFactory.createTexture(2, 2, Color.BLUE);
            context.getRenderManager().register("event", new SpriteRenderer(texture));
        }

        SharedAssetManager.getInstance().load(Assets.TiledMaps.INTRO, TiledMap.class);
        TiledMap map = SharedAssetManager.getInstance().get(Assets.TiledMaps.INTRO, TiledMap.class);
        context.getTiledMapManager().load(map, context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);

        final Texture johnTexture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER_TILESET);
        SpriteSheet johnSheet = new SpriteSheet(johnTexture, 4, 4);
        CharacterInitializer.createAnimations(context, johnSheet, CharacterType.JOHN).scale(1, 2f);

        final Texture demonTexture = SharedAssetManager.getInstance().get(Assets.Textures.KALMAG_TILESET);
        SpriteSheet demonSheet = new SpriteSheet(demonTexture, 8, 4);
        CharacterInitializer.createAnimations(context, demonSheet, CharacterType.KALMAG).scale(1, 2f);

        final Texture dogTexture = SharedAssetManager.getInstance().get(Assets.Textures.DOG_TILESET);
        SpriteSheet dogSheet = new SpriteSheet(dogTexture, 4, 4);
        CharacterInitializer.createAnimations(context, dogSheet, CharacterType.DOG).scale(1, 2f);

        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setLutIntensity(0.7f);
        vignette.setIntensity(0.9f);
        vignette.setSaturationMul(1.1f);
        context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(vignette);

        // CORE components
        context.getEventManager().register(new SpeechHandler(context.getStage()), SayEvent.class);
        loader.load(Assets.TiledMaps.INTRO);

        // Creepy stuff
        Music soundscape = SharedAssetManager.getInstance().get(Assets.Musics.SOUNDSCAPE, Music.class);
        soundscape.setLooping(true);
        soundscape.setVolume(0.15f);
        soundscape.play();

        cameraController = new CameraController(context.getGameCamera());

        context.getRenderManager().register("page", new SpriteRenderer(Assets.Textures.PAGE));
    }

    @Override
    protected void onUpdate(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        cameraController.update(delta);
        loader.update();
    }
}
