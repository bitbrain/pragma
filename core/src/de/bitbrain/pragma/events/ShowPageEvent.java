package de.bitbrain.pragma.events;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;

public class ShowPageEvent implements GameEvent {

    private String text;
    private String caption;

    public ShowPageEvent(String caption, String text) {
        this.caption = caption;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getCaption() {
        return caption;
    }
}
