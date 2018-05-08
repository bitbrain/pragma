package de.bitbrain.pragma.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.input.OrientationMovementController;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.events.LevelLoadEvent;

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
                context.getTiledMapManager().getAPI().setDebug(true);
                context.getTiledMapManager().load(map, context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);
                GameObject player = null;
                for (GameObject o : context.getGameWorld()) {
                    if (CharacterType.JOHN.name().equals(o.getType())) {
                        player = o;
                    }
                    if ("tree_light".equals(o.getType())) {
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 200f, o.getColor());
                    }
                    if ("car_light_front".equals(o.getType())) {
                        context.getLightingManager().addConeLight(UUID.randomUUID().toString(),  o.getLeft(), o.getTop(), 320f, 0f, 15f,
                        Color.YELLOW);
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 50f, Color.YELLOW);
                    }
                    if ("car_light_back".equals(o.getType())) {
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 50f, Color.RED);
                    }
                }

                context.getLightingManager().setAmbientLight(Color.valueOf("#111144"));

                context.getGameCamera().setTarget(player);
                context.getGameCamera().setBaseZoom(340f / Gdx.graphics.getWidth());
                context.getGameCamera().setZoomScale(0.0001f);
                context.getGameCamera().setSpeed(0.7f);
                context.getGameCamera().setStickToWorldBounds(true);

                OrientationMovementController controller = new OrientationMovementController();
                RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller, context.getTiledMapManager().getAPI())
                        .interval(0.8f)
                        .rasterSize(context.getTiledMapManager().getAPI().getCellWidth(), context.getTiledMapManager().getAPI().getCellHeight());
                context.getBehaviorManager().apply(behavior, player);

                context.getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("#333333"), 180f, context.getLightingManager()), player);

                EventHandler eventHandler = new EventHandler(context.getEventManager());
                context.getBehaviorManager().apply(eventHandler);
                context.getEventManager().publish(new LevelLoadEvent());
            }
            level = null;
        }
    }
}
