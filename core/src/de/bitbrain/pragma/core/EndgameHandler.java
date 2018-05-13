package de.bitbrain.pragma.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Config;
import de.bitbrain.pragma.ai.DevilController;
import de.bitbrain.pragma.events.EndgameEvent;
import de.bitbrain.pragma.events.SayEvent;
import de.bitbrain.pragma.graphics.ScreenShake;

public class EndgameHandler implements GameEventListener<EndgameEvent> {

    private final GameContext context;
    private final RasteredMovementBehavior movementBehavior;
    private final GameObject player;

    public EndgameHandler(GameContext context, RasteredMovementBehavior movementBehavior, GameObject player) {
        this.context = context;
        this.movementBehavior = movementBehavior;
        this.player = player;
    }

    @Override
    public void onEvent(EndgameEvent event) {
        GameObject producer = event.getProducer();
        for (GameObject o : context.getGameWorld()) {
            if ("KALMAG".equals(o.getType())) {
                o.getColor().a = 1f;
                context.getBehaviorManager().apply(new PointLightBehavior(Color.RED, 200f, context.getLightingManager()), o);
                context.getParticleManager().attachEffect(Assets.Particles.AURA, o, 32f, 32f);
                final DevilController devilController = new DevilController(player, o, context.getBehaviorManager(), context.getTiledMapManager());
                context.getAudioManager().spawnSound(Assets.Sounds.CREATURE, o.getLeft(), o.getTop(), 1f, 1f, 300f);
                context.getAudioManager().playMusic(Assets.Musics.ESCAPE);
                ScreenShake.shake(5f, 2f);

                context.getEventManager().publish(new SayEvent(player, "WHAT IS THIS?! I NEED TO GET BACK TO THE CAR!!!"));
                /*if (Config.DEBUG) {
                    context.getRenderPipeline().putAfter(RenderPipeIds.PARTICLES, "devil-path", new RenderLayer() {

                        private Texture texture = GraphicsFactory.createTexture(2, 2, Color.WHITE);

                        @Override
                        public void beforeRender() {

                        }

                        @Override
                        public void render(Batch batch, float delta) {
                            Path path = devilController.getPath();
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
                }*/
                break;
            }
        }

        context.getLightingManager().setAmbientLight(Color.valueOf("220011"));
        movementBehavior.interval(0.2f);
    }
}
