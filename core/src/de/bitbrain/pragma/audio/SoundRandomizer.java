package de.bitbrain.pragma.audio;

import com.badlogic.gdx.audio.Sound;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

import java.util.Random;

/**
 * Randomises sounds and plays them attached on an object
 */
public class SoundRandomizer extends BehaviorAdapter {

    private final Random random = new Random();
    private final GameContext context;
    private final DeltaTimer deltaTimer = new DeltaTimer();
    private final float minInterval, maxInterval;
    private final float distance;
    private final String[] ids;

    private float currentInterval;

    public SoundRandomizer(GameContext context, float minInterval, float maxInterval, float distance, String... ids) {
        this.context = context;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.distance = distance;
        this.ids = ids;
        currentInterval = computeNextInterval();
    }

    @Override
    public void update(GameObject source, float delta) {
        deltaTimer.update(delta);
        if (deltaTimer.reached(currentInterval)) {
            deltaTimer.reset();
            currentInterval = computeNextInterval();
            context.getAudioManager().spawnSound(//
                    getRandomSoundId(),//
                    source.getLeft(),//
                    source.getTop(),//
                    0.5f + random.nextFloat(),//
                    1f,//
                    distance//
            );
        }
    }

    private float computeNextInterval() {
        return minInterval + (float)Math.random() * (maxInterval - minInterval);
    }

    private String getRandomSoundId() {
        return ids[(int)Math.floor(ids.length * random.nextFloat())];
    }
}
