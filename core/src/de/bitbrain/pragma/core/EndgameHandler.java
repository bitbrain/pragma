package de.bitbrain.pragma.core;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.ai.DevilController;
import de.bitbrain.pragma.events.EndgameEvent;
import de.bitbrain.pragma.events.SayEvent;
import de.bitbrain.pragma.graphics.ScreenShake;

public class EndgameHandler implements GameEventListener<EndgameEvent> {

    private final GameContext context;
    private final RasteredMovementBehavior movementBehavior;
    private final GameObject player;
    private DevilController devilController;

    public EndgameHandler(GameContext context, RasteredMovementBehavior movementBehavior, GameObject player) {
        this.context = context;
        this.movementBehavior = movementBehavior;
        this.player = player;
    }

    public Path getPath() {
        if (devilController == null) {
            return null;
        }
        return devilController.getPath();
    }

    @Override
    public void onEvent(EndgameEvent event) {
        for (GameObject o : context.getGameWorld()) {
            if ("KALMAG".equals(o.getType())) {
                o.getColor().a = 1f;
                context.getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("#ff0044"), 400f, context.getLightingManager()), o);
                context.getParticleManager().attachEffect(Assets.Particles.AURA, o, 32f, 32f);
                context.getAudioManager().spawnSound(Assets.Sounds.CREATURE, o.getLeft(), o.getTop(), 1f, 1f, 300f);
                context.getAudioManager().playMusic(Assets.Musics.ESCAPE);
                ScreenShake.shake(5f, 2f);
                context.getEventManager().publish(new SayEvent(player, "WHAT IS THIS?! I NEED TO GET BACK TO THE CAR!!!"));
                final GameObject devil = o;
                Tween.to(o, GameObjectTween.ALPHA, 3f).target(1f)//
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                devilController = new DevilController(devil, player, context.getBehaviorManager(), context.getTiledMapManager(), context.getEventManager());
                            }
                        })//
                        .setCallbackTriggers(TweenCallback.COMPLETE)//
                        .start(context.getTweenManager());
                break;
            }
        }

        context.getLightingManager().setAmbientLight(Color.valueOf("220011"));
        movementBehavior.interval(0.2f);
    }
}
