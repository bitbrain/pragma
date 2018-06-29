package de.bitbrain.pragma.ai;

import com.badlogic.gdx.Game;

import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.MovementController;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.tmx.IndexCalculator;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.tmx.TiledMapListenerAdapter;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.world.GameObject;

public class ChasingBehavior extends BehaviorAdapter {

    public static interface ChasingListener {
        void onArriveTarget();
    }

    private Path path;
    private final RasteredMovementBehavior behavior;
    private int previousLength = 0;
    private int minLength = 0;
    private ChasingListener listener;
    private boolean targetArrived = false;

    private final TiledMapListenerAdapter pathUpdater = new TiledMapListenerAdapter() {

        @Override
        public void onEnterCell(int xIndex, int yIndex, GameObject object, TiledMapAPI api) {
            if (target != null) {
                int sourceX = IndexCalculator.calculateIndex(source.getLeft(), tiledMapManager.getAPI().getCellWidth());
                int sourceY = IndexCalculator.calculateIndex(source.getTop(), tiledMapManager.getAPI().getCellHeight());

                int targetX = IndexCalculator.calculateIndex(target.getLeft(), tiledMapManager.getAPI().getCellWidth());
                int targetY = IndexCalculator.calculateIndex(target.getTop(), tiledMapManager.getAPI().getCellHeight());

                // Finding the direct path
                path = tiledMapManager.getPathFinder().findPath(source, targetX, targetY);

                if (sourceX == targetX && sourceY == targetY) {
                    // We do not recalculate when we are on the target
                    return;
                }

                // Direct path not available, finding nearby path
                if (path == null) {
                    for (int x = targetX - 1; x <= targetX + 1; x += 1) {
                        for (int y = targetY - 1; y <= targetY + 1; y += 1) {
                            path = tiledMapManager.getPathFinder().findPath(source, x, y);
                            if (path != null) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    };


    private final MovementController<Orientation> chasingController = new MovementController<Orientation>() {
        @Override
        public void update(Movement movement, float delta) {
            if (path == null || (path.getLength() - 1) <= minLength) {
                if (!targetArrived && listener != null && (path == null || previousLength > path.getLength())) {
                    listener.onArriveTarget();
                    targetArrived = true;
                }
                return;
            } else {
                targetArrived = false;
            }
            previousLength = path.getLength();
            if (!movement.isMoving()) {
                int indexX = IndexCalculator.calculateIndex(source.getLeft(), tiledMapManager.getAPI().getCellWidth());
                int indexY = IndexCalculator.calculateIndex(source.getTop(), tiledMapManager.getAPI().getCellHeight());
                int pathX = getPath().getX(1);
                int pathY = getPath().getY(1);
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
                    getPath().remove(1);
                }
            }
        }
    };

    private final TiledMapManager tiledMapManager;
    private GameObject target;
    private final GameObject source;

    @Override
    public void update(GameObject source, float delta) {
        if (source.equals(this.source)) {
            behavior.update(source, delta);
        }
    }

    public void setListener(ChasingListener listener) {
        this.listener = listener;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public RasteredMovementBehavior getMovement() {
        return behavior;
    }

    public ChasingBehavior(GameObject source, GameObject target, TiledMapManager tiledMapManager) {
        this.tiledMapManager = tiledMapManager;
        this.source = source;
        this.target = target;
        this.tiledMapManager.addListener(pathUpdater);
        behavior = new RasteredMovementBehavior(chasingController, tiledMapManager.getAPI())
                .interval(0.25f)
                .rasterSize(tiledMapManager.getAPI().getCellWidth(), tiledMapManager.getAPI().getCellHeight());
    }

    public Path getPath() {
        return path;
    }
}
