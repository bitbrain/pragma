package de.bitbrain.pragma.ai;

import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.tmx.IndexCalculator;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.tmx.TiledMapListenerAdapter;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.world.GameObject;

public class DevilController {

    private Path path;

    private final BehaviorAdapter chasingBehavior = new BehaviorAdapter() {
        @Override
        public void update(GameObject source, float delta) {
        }
    };

    private final TiledMapListenerAdapter pathUpdater = new TiledMapListenerAdapter() {

        @Override
        public void onEnterCell(int xIndex, int yIndex, GameObject object, TiledMapAPI api) {
            int playerX = IndexCalculator.calculateIndex(player.getLeft(), tiledMapManager.getAPI().getCellWidth());
            int playerY = IndexCalculator.calculateIndex(player.getTop(), tiledMapManager.getAPI().getCellHeight());
            path = tiledMapManager.getPathFinder().findPath(devil, playerX, playerY);
        }
    };

    private final TiledMapManager tiledMapManager;
    private final GameObject player;
    private final GameObject devil;

    public DevilController(GameObject devil, GameObject player, BehaviorManager behaviorManager, TiledMapManager tiledMapManager) {
        this.tiledMapManager = tiledMapManager;
        this.devil = devil;
        this.player = player;
        this.tiledMapManager.addListener(pathUpdater);
        behaviorManager.apply(chasingBehavior, devil);
    }

    public Path getPath() {
        return path;
    }
}
