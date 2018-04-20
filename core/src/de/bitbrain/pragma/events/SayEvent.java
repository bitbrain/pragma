package de.bitbrain.pragma.events;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;

public class SayEvent implements GameEvent {

    private final String text;
    private final GameObject producer;

    public SayEvent(GameObject producer, String text) {
        this.text = text;
        this.producer = producer;
    }

    public String getText() {
        return text;
    }

    public GameObject getProducer() {
        return producer;
    }
}
