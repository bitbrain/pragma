package de.bitbrain.pragma.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.UUID;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.input.OrientationMovementController;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.tmx.TiledMapListener;
import de.bitbrain.braingdx.tmx.TiledMapListenerAdapter;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;

public class LevelLoader {

    private String level;
    private final GameContext context;


    public LevelLoader(GameContext context) {
        this.context = context;
    }

    public void load(String level) {
        this.level = level;
    }

    public void update() {
        if (level != null) {
            Gdx.app.log("LEVEL", "LOAD " + level);
            TiledMap map = SharedAssetManager.getInstance().get(level, TiledMap.class);
            if (map != null) {
                // Clearing lighting
                context.getLightingManager().clear();
                context.getTiledMapManager().load(map, context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);
                GameObject player = null;
                for (GameObject o : context.getGameWorld()) {
                    if (CharacterType.JOHN.name().equals(o.getType())) {
                        player = o;
                    }
                    if (CharacterType.KALMAG.name().equals(o.getType())) {
                        o.setDimensions(64, 32);
                        context.getBehaviorManager().apply(new PointLightBehavior(Color.RED, 200f, context.getLightingManager()), o);
                    }
                    if ("tree_light".equals(o.getType())) {
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 200f, o.getColor());
                    }
                    if ("car_light_front".equals(o.getType())) {
                        Color color = Color.valueOf("ffecac");
                        color.a = 0.5f;
                        context.getLightingManager().addConeLight(UUID.randomUUID().toString(),  o.getLeft(), o.getTop(), 320f, 0f, 15f, Color.valueOf("ffecac"));
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 70f, color);
                    }
                    if ("car_light_back".equals(o.getType())) {
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 50f, Color.RED);
                    }
                    if ("blood_light".equals(o.getType())) {
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 200f, Color.valueOf("#330000"));
                    }
                    if ("engine_sound".equals(o.getType())) {
                        context.getAudioManager().spawnSoundLooped(Assets.Sounds.ENGINE_RUNNING, o.getLeft(), o.getTop(), 1f, 1f, 420f);
                    }
                }

                if (player == null) {
                    throw new GdxRuntimeException("No player initialised! Create an object of type '" + CharacterType.JOHN.name() + "'");
                }

                // FOOTSTEPS
                context.getBehaviorManager().apply(new BehaviorAdapter() {

                    private float lastX, lastY;
                    @Override
                    public void update(GameObject source, float delta) {
                        if (lastX != source.getLastPosition().x || lastY != source.getLastPosition().y) {
                            lastX = source.getLastPosition().x;
                            lastY = source.getLastPosition().y;
                            SharedAssetManager.getInstance().get(Assets.Sounds.FOOTSTEP, Sound.class).play(0.3f, (float)Math.random() * 0.5f + 0.7f, 0f);
                        }
                    }
                }, player);

                context.getLightingManager().setAmbientLight(Color.valueOf("#111144"));
                // Setup camera
                context.getGameCamera().setTarget(player);
                context.getGameCamera().setBaseZoom(340f / Gdx.graphics.getWidth());
                context.getGameCamera().setZoomScale(0.0001f);
                context.getGameCamera().setSpeed(0.7f);
                context.getGameCamera().setStickToWorldBounds(true);

                // Setup player movement
                OrientationMovementController controller = new OrientationMovementController();
                RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller, context.getTiledMapManager().getAPI())
                        .interval(0.35f)
                        .rasterSize(context.getTiledMapManager().getAPI().getCellWidth(), context.getTiledMapManager().getAPI().getCellHeight());
                context.getBehaviorManager().apply(behavior, player);

                context.getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("#333333"), 180f, context.getLightingManager()), player);

                // Stick player to field to avoid collision issues
                TiledMapAPI api = context.getTiledMapManager().getAPI();
                float normalizedX = (float)Math.floor(player.getLeft() / api.getCellWidth()) * api.getCellWidth();
                float normalizedY = (float)Math.floor(player.getTop() / api.getCellHeight()) * api.getCellHeight();
                player.setPosition(normalizedX, normalizedY);
                player.setDimensions(32, 16);
                context.getBehaviorManager().apply(new EventHandler(context.getEventManager(), context.getGameWorld()));
            }
            level = null;
        }
    }
}
