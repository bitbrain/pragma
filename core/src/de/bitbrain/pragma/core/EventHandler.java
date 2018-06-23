package de.bitbrain.pragma.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashSet;
import java.util.Set;

import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.pragma.events.EscapeSuccessfulEvent;
import de.bitbrain.pragma.screens.EscapeSuccessfulScreen;

public class EventHandler implements Behavior {

    private final GameEventManager gameEventManager;
    private Rectangle sourceRect, targetRect;
    private final EventFactory eventFactory = new EventFactory();
    private Set<String> eventIds = new HashSet<String>();
    private GameWorld gameWorld;

    public EventHandler(GameEventManager gameEventManager, GameWorld gameWorld) {
        this.gameEventManager = gameEventManager;
        this.sourceRect = new Rectangle();
        this.targetRect = new Rectangle();
        this.gameWorld = gameWorld;
    }

    @Override
    public void onAttach(GameObject source) {
        // noOp
    }

    @Override
    public void onDetach(GameObject source) {
        // noOp
    }

    @Override
    public void update(GameObject source, float delta) {
        // noOp
    }

    @Override
    public void update(GameObject source, GameObject target, float delta) {
        if (!source.getType().equals("event") && !source.getType().equals("page")) {
            return;
        }
        // Events do not collide!
        if (source.getType().equals(target.getType())) {
            return;
        }

        MapProperties properties = (MapProperties)source.getAttribute(MapProperties.class);

        if (properties.containsKey("producer") && !properties.get("producer").equals(target.getType())) {
            return;
        }

        sourceRect.set(source.getLeft(), source.getTop(), source.getWidth(), source.getHeight());
        targetRect.set(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight());

        if (sourceRect.contains(targetRect) || sourceRect.overlaps(targetRect)) {
            if (eventIds.contains(source.getId())) {
                // Event already consumed!
                gameWorld.remove(source);
                return;
            }
            eventIds.add(source.getId());
            // Source is the event!
            GameEvent event = eventFactory.create(source, target);
            if (event != null) {
                gameEventManager.publish(event);
            } else {
                Gdx.app.log("WARN", "Unable to publish event for " + source + "! Not supported by EventFactory!");
            }
        } else if (properties.containsKey("sticky") && (Boolean)properties.get("sticky")) {
            if (eventIds.contains(source.getId())) {
               eventIds.remove(source.getId());
            }
        }

    }
}
