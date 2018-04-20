package de.bitbrain.pragma.core;

import com.badlogic.gdx.maps.MapProperties;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.events.SayEvent;
import de.bitbrain.pragma.events.TeleportEvent;

class EventFactory {

    GameEvent create(GameObject event, GameObject producer) {
        if (!event.getType().equals("event")) {
            return null;
        }
        MapProperties mapProperties = (MapProperties)event.getAttribute(MapProperties.class);
        if (mapProperties.containsKey("say")) {
            return new SayEvent(producer, (String)mapProperties.get("say"));
        }
        if (mapProperties.containsKey("teleport")) {
            String expression = (String)mapProperties.get("teleport");
            String door = expression;
            String level = expression.substring(expression.lastIndexOf("=") + 1);
            if (level.contains(";")) {
                door = expression.substring(expression.lastIndexOf(";") + 1);
            }
            level = "maps/" + level.replace(";" + door, "") + ".tmx";
            return new TeleportEvent(level, door);
        }
        return null;
    }
}
