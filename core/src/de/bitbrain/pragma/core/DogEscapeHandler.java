package de.bitbrain.pragma.core;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.audio.Sound;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.ai.ChasingBehavior;
import de.bitbrain.pragma.events.DogRunsAwayEvent;
import de.bitbrain.pragma.events.SayEvent;

public class DogEscapeHandler implements GameEventListener<DogRunsAwayEvent>, ChasingBehavior.ChasingListener {

    private final GameObject dogTarget;
    private final GameObject dog;
    private final ChasingBehavior dogChasingBehavior;
    private final RasteredMovementBehavior playerMovement;
    private final GameContext context;
    private boolean triggered = false;

    public DogEscapeHandler(GameObject target, ChasingBehavior chasingBehavior, RasteredMovementBehavior playerMovement, GameObject dog, GameContext context) {
        this.dogTarget = target;
        this.dog = dog;
        this.dogChasingBehavior = chasingBehavior;
        this.context = context;
        this.playerMovement = playerMovement;
        SharedAssetManager.getInstance().get(Assets.Sounds.WHINING, Sound.class).play(0.5f);
    }

    @Override
    public void onEvent(DogRunsAwayEvent event) {
        if (!triggered) {
            dogChasingBehavior.setTarget(dogTarget);
            dogChasingBehavior.setMinLength(0);
            dogChasingBehavior.getMovement().interval(0.15f);
            context.getEventManager().publish(new SayEvent(dog, "MERLIN!! Wait!!\nI need to chase him!!"));
            SharedAssetManager.getInstance().get(Assets.Sounds.SHOCK, Sound.class).play();
            Tween.call(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    context.getAudioManager().spawnSoundLooped(Assets.Sounds.BARK, dog, 1f, 0.7f, 400f);
                }
            }).delay(1f).start(SharedTweenManager.getInstance());
            dogChasingBehavior.setListener(this);
            playerMovement.interval(0.27f);
            triggered = false;
        }

    }

    @Override
    public void onArriveTarget() {
        dog.setActive(false);
        dog.setPosition(-999,-999);
        dog.getColor().a = 0f;
        dogChasingBehavior.setTarget(null);
        dogChasingBehavior.setListener(null);
    }
}
