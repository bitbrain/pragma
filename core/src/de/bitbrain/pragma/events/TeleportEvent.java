package de.bitbrain.pragma.events;

import de.bitbrain.braingdx.event.GameEvent;

public class TeleportEvent implements GameEvent {

    private final String level;

    private final String door;

    public TeleportEvent(String level, String door) {
        this.level = level;
        this.door = door;
    }

    public String getLevel() {
        return level;
    }
    public String getDoor() { return door; }
}
