package de.bitbrain.pragma.core;

import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.ai.ChasingBehavior;
import de.bitbrain.pragma.events.DogRunsAwayEvent;
import de.bitbrain.pragma.events.SayEvent;

public class DogEscapeHandler implements GameEventListener<DogRunsAwayEvent>, ChasingBehavior.ChasingListener {

    private final GameObject dogTarget;
    private final GameObject dog;
    private final ChasingBehavior chasingBehavior;
    private final GameEventManager eventManager;

    public DogEscapeHandler(GameObject target, ChasingBehavior chasingBehavior, GameObject dog, GameEventManager eventManager) {
        this.dogTarget = target;
        this.dog = dog;
        this.chasingBehavior = chasingBehavior;
        this.eventManager = eventManager;
    }

    @Override
    public void onEvent(DogRunsAwayEvent event) {
        chasingBehavior.setTarget(dogTarget);
        chasingBehavior.getMovement().interval(0.1f);
        eventManager.publish(new SayEvent(dog, "Nooo!! Wait!!\nI have to chase him!!"));
        chasingBehavior.setListener(this);
    }

    @Override
    public void onArriveTarget() {
        dog.setActive(false);
        dog.getColor().a = 0f;
        chasingBehavior.setTarget(null);
        chasingBehavior.setListener(null);
    }
}
