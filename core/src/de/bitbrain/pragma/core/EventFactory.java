package de.bitbrain.pragma.core;

import com.badlogic.gdx.maps.MapProperties;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.events.DogRunsAwayEvent;
import de.bitbrain.pragma.events.EndgameEvent;
import de.bitbrain.pragma.events.EscapeSuccessfulEvent;
import de.bitbrain.pragma.events.SayEvent;
import de.bitbrain.pragma.events.ShowPageEvent;

class EventFactory {

    GameEvent create(GameObject event, GameObject producer) {
        if (!event.getType().equals("event") && !event.getType().equals("page")) {
            return null;
        }
        MapProperties mapProperties = (MapProperties)event.getAttribute(MapProperties.class);

        if (mapProperties.containsKey("endgame")) {
            return new EndgameEvent(event);
        }
        if (mapProperties.containsKey("say")) {
            return new SayEvent(producer, (String)mapProperties.get("say"));
        }
        if (mapProperties.containsKey("caption")) {
            return new ShowPageEvent((String)mapProperties.get("caption"), (String)mapProperties.get("text"));
        }
        if (mapProperties.containsKey("safezone")) {
            return new EscapeSuccessfulEvent();
        }
        if (mapProperties.containsKey("dog_runs_away")) {
            return new DogRunsAwayEvent();
        }
        return null;
    }
}
