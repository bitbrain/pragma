package de.bitbrain.pragma.graphics;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.AnimationType;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationFactory;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationSupplier;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.graphics.renderer.AnimationRenderer;
import de.bitbrain.pragma.core.CharacterType;

public class CharacterInitializer {

    public static void createAnimations(GameContext context, SpriteSheet sheet) {
        Map<Integer, SpriteSheetAnimationFactory.Index> indices = createSpriteIndices();
        SpriteSheetAnimationFactory animationFactory = new SpriteSheetAnimationFactory(sheet, indices);
        for (int type : indices.keySet()) {
            SpriteSheetAnimation animation = animationFactory
                    .create(type)
                    .base(0)
                    .interval(0.1f)
                    .direction(SpriteSheetAnimation.Direction.HORIZONTAL)
                    .frames(4)
                    .origin(0, 0)
                    .scale(1f, 2f)
                    .source(sheet);
            SpriteSheetAnimationSupplier supplier = new SpriteSheetAnimationSupplier(orientations(), animation,
                    AnimationTypes.FORWARD);
            context.getBehaviorManager().apply(supplier);
            context.getRenderManager().register(CharacterType.values()[type].name(), new AnimationRenderer(supplier));
        }
    }

    private static Map<Orientation, Integer> orientations() {
        Map<Orientation, Integer> map = new HashMap<Orientation, Integer>();
        map.put(Orientation.DOWN, 0);
        map.put(Orientation.UP, 1);
        map.put(Orientation.LEFT, 2);
        map.put(Orientation.RIGHT, 3);
        return map;
    }

    private static Map<Integer, SpriteSheetAnimationFactory.Index> createSpriteIndices() {
        Map<Integer, SpriteSheetAnimationFactory.Index> indices = new HashMap<Integer, SpriteSheetAnimationFactory.Index>();
        indices.put(CharacterType.JOHN.ordinal(), new SpriteSheetAnimationFactory.Index(0, 0));
        return indices;
    }
}
