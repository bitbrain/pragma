package de.bitbrain.pragma.ai;

import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.MovementController;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.tmx.IndexCalculator;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.tmx.TiledMapListenerAdapter;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.events.GameOverEvent;

public class DevilController {

    private Path path;

    private Behavior gameOverLogic = new BehaviorAdapter() {

        private boolean gameOver = false;
        @Override
        public void update(GameObject source, float delta) {
            if (!gameOver && path != null && path.getLength() <= 2) {
                gameOver = true;
                eventManager.publish(new GameOverEvent());
            }
        }
    };

    private final TiledMapListenerAdapter pathUpdater = new TiledMapListenerAdapter() {

        @Override
        public void onEnterCell(int xIndex, int yIndex, GameObject object, TiledMapAPI api) {
            int devilX = IndexCalculator.calculateIndex(devil.getLeft(), tiledMapManager.getAPI().getCellWidth());
            int devilY = IndexCalculator.calculateIndex(devil.getTop(), tiledMapManager.getAPI().getCellHeight());
            path = tiledMapManager.getPathFinder().findPath(player, devilX, devilY);
        }
    };


    private final MovementController<Orientation> chasingController = new MovementController<Orientation>() {
        @Override
        public void update(Movement movement, float delta) {
            if (path == null || path.getLength() <= 1) {
                return;
            }
            if (!movement.isMoving()) {
                int indexX = IndexCalculator.calculateIndex(devil.getLeft(), tiledMapManager.getAPI().getCellWidth());
                int indexY = IndexCalculator.calculateIndex(devil.getTop(), tiledMapManager.getAPI().getCellHeight());
                int pathX = getPath().getX(getPath().getLength() - 2);
                int pathY = getPath().getY(getPath().getLength() - 2);
                float deltaX = pathX - indexX;
                float deltaY = pathY - indexY;
                if (deltaX > 0) {
                    movement.move(Orientation.RIGHT);
                } else if (deltaX < 0) {
                    movement.move(Orientation.LEFT);
                } else if (deltaY > 0) {
                    movement.move(Orientation.UP);
                } else if (deltaY < 0) {
                    movement.move(Orientation.DOWN);
                }
                if (deltaX != 0 || deltaY != 0) {
                    getPath().remove(getPath().getLength() - 2);
                }
            }
        }
    };

    private final TiledMapManager tiledMapManager;
    private final GameEventManager eventManager;
    private final GameObject player;
    private final GameObject devil;

    public DevilController(GameObject devil, GameObject player, BehaviorManager behaviorManager, TiledMapManager tiledMapManager, GameEventManager eventManager) {
        this.tiledMapManager = tiledMapManager;
        this.eventManager = eventManager;
        this.devil = devil;
        this.player = player;
        this.tiledMapManager.addListener(pathUpdater);
        RasteredMovementBehavior behavior = new RasteredMovementBehavior(chasingController, tiledMapManager.getAPI())
                .interval(0.25f)
                .ease(TweenEquations.easeOutCubic)
                .rasterSize(tiledMapManager.getAPI().getCellWidth(), tiledMapManager.getAPI().getCellHeight());
        behaviorManager.apply(behavior, devil);
        behaviorManager.apply(gameOverLogic);
    }

    public Path getPath() {
        return path;
    }
}
