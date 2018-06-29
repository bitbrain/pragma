package de.bitbrain.pragma.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.ai.ChasingBehavior;
import de.bitbrain.pragma.audio.SoundRandomizer;
import de.bitbrain.pragma.events.EndgameEvent;
import de.bitbrain.pragma.events.GameOverEvent;
import de.bitbrain.pragma.events.SayEvent;
import de.bitbrain.pragma.graphics.ScreenShake;

public class EndgameHandler implements GameEventListener<EndgameEvent> {

    private final GameContext context;
    private final RasteredMovementBehavior movementBehavior;
    private final GameObject player;
    private final GameObject safezone;
    private ChasingBehavior chasingBehavior;

    private Behavior gameOverLogic = new BehaviorAdapter() {

        private boolean gameOver = false;
        @Override
        public void update(GameObject source, float delta) {
            if (!gameOver && chasingBehavior != null && chasingBehavior.getPath() != null && chasingBehavior.getPath().getLength() <= 3) {
               gameOver = true;
               context.getEventManager().publish(new GameOverEvent());
               SharedAssetManager.getInstance().get(Assets.Sounds.CREATE_2, Sound.class).play();
            }
        }
    };

    public EndgameHandler(GameContext context, RasteredMovementBehavior movementBehavior, GameObject player, GameObject safezone) {
        this.context = context;
        this.movementBehavior = movementBehavior;
        this.player = player;
        this.safezone = safezone;
    }

    public Path getPath() {
        if (chasingBehavior == null) {
            return null;
        }
        return chasingBehavior.getPath();
    }

    @Override
    public void onEvent(EndgameEvent event) {
        for (GameObject o : context.getGameWorld()) {
            if ("KALMAG".equals(o.getType())) {
                context.getBehaviorManager().apply(gameOverLogic);
                final GameObject devil = o;
                context.getLightingManager().setAmbientLight(Color.valueOf("220011"));
                context.getAudioManager().playMusic(Assets.Musics.ESCAPE);
                ScreenShake.shake(2f, 2f);
                context.getEventManager().publish(new SayEvent(player, "WH...WHAT IS HAPPENING?!"));
                movementBehavior.interval(2f);
                safezone.setActive(true);
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        context.getAudioManager().crossFadeMusic(Assets.Musics.ESCAPE, Assets.Musics.DANGER, 10f);
                    }
                }).delay(50f).start(context.getTweenManager());
                Tween.to(o, GameObjectTween.ALPHA, 0.1f).delay(4f).target(1f)//
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                context.getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("#fe0b5c"), 400f, context.getLightingManager()), devil);
                                context.getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("#ffffff"), 90f, context.getLightingManager()), devil);
                                context.getParticleManager().attachEffect(Assets.Particles.AURA, devil, 32f, 32f);
                                context.getAudioManager().spawnSound(Assets.Sounds.CREATURE, devil.getLeft(), devil.getTop(), 1f, 1f, 300f);
                                ScreenShake.shake(5f, 2f);
                                chasingBehavior = new ChasingBehavior(devil, player, context.getTiledMapManager());
                                chasingBehavior.getMovement().ease(TweenEquations.easeOutCubic);
                                chasingBehavior.setMinLength(1);
                                context.getBehaviorManager().apply(chasingBehavior);
                                movementBehavior.interval(0.2f);
                                context.getBehaviorManager().apply(new SoundRandomizer(
                                        context,
                                        10,
                                        20,
                                        400,
                                        Assets.Sounds.CREATURE,
                                        Assets.Sounds.CREATE_2
                                ), devil);
                                context.getBehaviorManager().apply(new SoundRandomizer(
                                        context,
                                        1f,
                                        1f,
                                        400,
                                        Assets.Sounds.CREATURE_STEP
                                ), devil);
                            }
                        })//
                        .setCallbackTriggers(TweenCallback.COMPLETE)//
                        .start(context.getTweenManager());
                break;
            }
        }
    }
}
