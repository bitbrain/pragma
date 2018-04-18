package de.bitbrain.pragma.core;

import com.badlogic.gdx.maps.MapProperties;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.core.say.SayEvent;

class EventFactory {

    GameEvent create(GameObject event, GameObject producer) {
        if (!event.getType().equals("event")) {
            return null;
        }
        MapProperties mapProperties = (MapProperties)event.getAttribute(MapProperties.class);
        if (mapProperties.containsKey("say")) {
            return new SayEvent(producer, (String)mapProperties.get("say"));
        }
        return null;
    }
}
