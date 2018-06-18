package de.bitbrain.pragma.core;

import com.badlogic.gdx.audio.Sound;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventListener;
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
        SharedAssetManager.getInstance().get(Assets.Sounds.WHINING, Sound.class).play(0.3f);
    }

    @Override
    public void onEvent(DogRunsAwayEvent event) {
        if (!triggered) {
            dogChasingBehavior.setTarget(dogTarget);
            dogChasingBehavior.getMovement().interval(0.1f);
            context.getEventManager().publish(new SayEvent(dog, "Nooo!! Wait!!\nI have to chase him!!"));
            context.getAudioManager().spawnSoundLooped(Assets.Sounds.BARK, dog.getLeft(), dog.getTop(), 1f, 0.7f, 600f);
            dogChasingBehavior.setListener(this);
            playerMovement.interval(0.27f);
            triggered = false;
        }

    }

    @Override
    public void onArriveTarget() {
        dog.setActive(false);
        dog.getColor().a = 0f;
        dogChasingBehavior.setTarget(null);
        dogChasingBehavior.setListener(null);
    }
}
