package de.bitbrain.pragma.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.input.OrientationMovementController;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapConfig;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.core.CharacterType;
import de.bitbrain.pragma.graphics.CharacterInitializer;


public class IngameScreen extends AbstractScreen<BrainGdxGame> {

    private GameObject player;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        setBackgroundColor(Colors.BACKGROUND);
        context.getScreenTransitions().in(5.5f);
        SharedAssetManager.getInstance().load(Assets.TiledMaps.INTRO, TiledMap.class);
        SharedAssetManager.getInstance().finishLoading();
        TiledMap map = SharedAssetManager.getInstance().get(Assets.TiledMaps.INTRO, TiledMap.class);
        context.getTiledMapManager().load(map, context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);

        final Texture texture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER_TILESET);
        SpriteSheet sheet = new SpriteSheet(texture, 4, 4);
        CharacterInitializer.createAnimations(context, sheet);

        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setLutIntensity(0.7f);
        vignette.setIntensity(0.7f);
        vignette.setSaturationMul(1.1f);

        context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(vignette);

        for (GameObject o : context.getGameWorld()) {
            if (o.getType().equals(CharacterType.JOHN.name())) {
                player = o;
            }
            if (o.getType().equals("tree_light")) {
                context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 200f, o.getColor());
            }
        }
        context.getGameCamera().setTarget(player);
        context.getGameCamera().setBaseZoom(0.17f);
        context.getGameCamera().setZoomScale(0.0001f);
        context.getGameCamera().setSpeed(0.7f);

        OrientationMovementController controller = new OrientationMovementController();
        RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller, context.getTiledMapManager().getAPI())
                .interval(0.8f)
                .rasterSize(context.getTiledMapManager().getAPI().getCellWidth(), context.getTiledMapManager().getAPI().getCellHeight());
        context.getBehaviorManager().apply(behavior, player);

        context.getLightingManager().setAmbientLight(Color.valueOf("#221166"));
        context.getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("#444444"), 300f, context.getLightingManager()), player);

    }
}
