package de.bitbrain.pragma.events;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;

public class EndgameEvent implements GameEvent {

    private final GameObject producer;

    public EndgameEvent(GameObject producer) {
        this.producer = producer;
    }

    public GameObject getProducer() {
        return producer;
    }
}
