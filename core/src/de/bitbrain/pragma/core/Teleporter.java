package de.bitbrain.pragma.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.pragma.events.LevelLoadEvent;
import de.bitbrain.pragma.events.TeleportEvent;

public class Teleporter {

    private final LevelLoader loader;
    private final GameWorld world;
    private TeleportEvent lastTeleportEvent;

    public Teleporter(LevelLoader loader, GameWorld world, GameEventManager eventManager) {
        this.loader = loader;
        this.world = world;
        eventManager.register(teleportListener, TeleportEvent.class);
        eventManager.register(levelLoadListener, LevelLoadEvent.class);
    }

    private final GameEventListener<TeleportEvent> teleportListener = new GameEventListener<TeleportEvent>() {
        @Override
        public void onEvent(TeleportEvent event) {
            String level = event.getLevel();
            if (level != null) {
                loader.load(event.getLevel());
            } else {
                teleport(event.getDoor());
            }
            lastTeleportEvent = event;
        }
    };

    private final GameEventListener<LevelLoadEvent> levelLoadListener = new GameEventListener<LevelLoadEvent>() {
        @Override
        public void onEvent(LevelLoadEvent event) {
            if (lastTeleportEvent != null) {
                teleport(lastTeleportEvent.getDoor());
            }
        }
    };

    private void teleport(String doorId) {
        GameObject door = getDoor(doorId);
        if (door != null) {
            GameObject player = getPlayer();
            if (player != null) {
                Gdx.app.log("TELEPORT", "Moved player to " + door.getLeft() + "," + door.getTop());
                player.setPosition(door.getLeft(), door.getTop());
            }
        }
    }

    private GameObject getPlayer() {
        for (GameObject o : world) {
            if ("JOHN".equals(o.getType())) {
                return o;
            }
        }
        return null;
    }

    private GameObject getDoor(String id) {
        // Find the correct door
        for (GameObject o : world) {
            if ("door".equals(o.getType())) {
                if (o.hasAttribute(MapProperties.class)) {
                    MapProperties properties = (MapProperties) o.getAttribute(MapProperties.class);
                    if (properties.get("id").equals(id)) {
                        return o;
                    }
                }
            }
        }
        return null;
    }
}
