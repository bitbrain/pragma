package de.bitbrain.pragma.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.UUID;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.input.OrientationMovementController;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Config;
import de.bitbrain.pragma.events.EndgameEvent;
import de.bitbrain.pragma.events.EscapeSuccessfulEvent;
import de.bitbrain.pragma.events.GameOverEvent;
import de.bitbrain.pragma.events.ShowPageEvent;
import de.bitbrain.pragma.screens.EscapeSuccessfulScreen;
import de.bitbrain.pragma.screens.GameOverScreen;
import de.bitbrain.pragma.ui.PageHandler;

public class LevelLoader {

    private String level;
    private final GameContext context;

    private final BrainGdxGame game;

    private GameObject player;

    private GameObject safeZone;

    public LevelLoader(GameContext context, BrainGdxGame game) {
        this.context = context;
        this.game = game;
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
                context.getTiledMapManager().getAPI().setDebug(Config.DEBUG);
                context.getTiledMapManager().load(map, context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);
                player = null;
                for (GameObject o : context.getGameWorld()) {
                    if (CharacterType.JOHN.name().equals(o.getType())) {
                        player = o;
                    }
                    if (CharacterType.KALMAG.name().equals(o.getType())) {
                        o.setDimensions(64, 32);
                        o.getColor().a = 0f;
                        TiledMapAPI api = context.getTiledMapManager().getAPI();
                        float normalizedX = (float)Math.floor(o.getLeft() / api.getCellWidth()) * api.getCellWidth();
                        float normalizedY = (float)Math.floor(o.getTop() / api.getCellHeight()) * api.getCellHeight();
                        o.setPosition(normalizedX, normalizedY);
                    }
                    if ("tree_light".equals(o.getType())) {
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 200f, o.getColor());
                    }
                    if ("blue_glimmer".equals(o.getType())) {
                        context.getLightingManager().addPointLight(UUID.randomUUID().toString(), new Vector2(o.getLeft(), o.getTop()), 80f, new Color(0f, 0f, 1f, 0.3f));
                    }
                    if ("event".equals(o.getType())) {
                        MapProperties mapProperties = (MapProperties)o.getAttribute(MapProperties.class);
                        if (mapProperties.containsKey("safezone")) {
                            o.setActive(false);
                            safeZone = o;
                        }
                    }
                    if ("page".equals(o.getType())) {
                        o.setDimensions(16f, 16f);
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
                final RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller, context.getTiledMapManager().getAPI())
                        .interval(0.35f)
                        .rasterSize(context.getTiledMapManager().getAPI().getCellWidth(), context.getTiledMapManager().getAPI().getCellHeight());
                context.getBehaviorManager().apply(behavior, player);

                context.getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("#181818"), 150f, context.getLightingManager()), player);

                // Stick player to field to avoid collision issues
                TiledMapAPI api = context.getTiledMapManager().getAPI();
                float normalizedX = (float)Math.floor(player.getLeft() / api.getCellWidth()) * api.getCellWidth();
                float normalizedY = (float)Math.floor(player.getTop() / api.getCellHeight()) * api.getCellHeight();
                player.setPosition(normalizedX, normalizedY);
                player.setDimensions(32, 16);
                context.getBehaviorManager().apply(new EventHandler(context.getEventManager(), context.getGameWorld()));


                context.getEventManager().register(new GameEventListener<GameOverEvent>() {
                    @Override
                    public void onEvent(GameOverEvent event) {
                        SharedAssetManager.getInstance().get(Assets.Musics.SOUNDSCAPE, Music.class).stop();
                        SharedAssetManager.getInstance().get(Assets.Musics.ESCAPE, Music.class).stop();
                        context.getAudioManager().clear();
                        context.getScreenTransitions().out(new GameOverScreen(game), 0.1f);
                    }
                }, GameOverEvent.class);

                context.getEventManager().register(new GameEventListener<EscapeSuccessfulEvent>() {
                    @Override
                    public void onEvent(EscapeSuccessfulEvent event) {
                        SharedAssetManager.getInstance().get(Assets.Musics.SOUNDSCAPE, Music.class).stop();
                        SharedAssetManager.getInstance().get(Assets.Musics.ESCAPE, Music.class).stop();
                        context.getAudioManager().clear();
                        context.getScreenTransitions().out(new EscapeSuccessfulScreen(game), 0.1f);
                    }
                }, EscapeSuccessfulEvent.class);

                final EndgameHandler endgameHandler = new EndgameHandler(context, behavior, player, safeZone);
                final PageHandler pageHandler = new PageHandler(context);

                context.getEventManager().register(endgameHandler, EndgameEvent.class);
                context.getEventManager().register(pageHandler, ShowPageEvent.class);

                if (Config.DEBUG) {
                    context.getRenderPipeline().putAfter(RenderPipeIds.PARTICLES, "devil-path", new RenderLayer() {

                        private Texture texture = GraphicsFactory.createTexture(2, 2, Color.WHITE);

                        @Override
                        public void beforeRender() {

                        }

                        @Override
                        public void render(Batch batch, float delta) {
                            Path path = endgameHandler.getPath();
                            if (path != null) {
                                batch.begin();
                                for (int i = 0; i < path.getLength(); ++i) {
                                    Color color = Color.valueOf("00ffff");
                                    color.a = 1f - MathUtils.clamp(5f / (i + 1f), 0.1f, 0.9f);
                                    batch.setColor(color);
                                    batch.draw(texture,
                                            path.getX(i) * context.getTiledMapManager().getAPI().getCellWidth(),
                                            path.getY(i) * context.getTiledMapManager().getAPI().getCellHeight(),
                                            context.getTiledMapManager().getAPI().getCellWidth(),
                                            context.getTiledMapManager().getAPI().getCellHeight());
                                }
                                batch.setColor(Color.WHITE);
                                batch.end();
                            }
                        }
                    });
                }
                context.getInput().addProcessor(pageHandler);
            }
            level = null;
        }
    }
}
