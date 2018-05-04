package de.bitbrain.pragma.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

import de.bitbrain.braingdx.graphics.ParallaxMap;
import de.bitbrain.braingdx.graphics.pipeline.AbstractRenderLayer;

public class ParallaxRenderLayer extends AbstractRenderLayer {

    private List<ParallaxMap> maps = new ArrayList<ParallaxMap>();

    private final Camera camera;

    public ParallaxRenderLayer(Camera camera) {
        this.camera = camera;
    }

    public ParallaxMap addLayer(Texture texture, float paralaxity) {
        ParallaxMap map = new ParallaxMap(texture, camera, paralaxity);
        maps.add(map);
        return map;
    }

    public ParallaxMap addLayer(String textureId, float paralaxity) {
        ParallaxMap map = new ParallaxMap(textureId, camera, paralaxity);
        maps.add(map);
        return map;
    }

    @Override
    public void render(Batch batch, float delta) {
        batch.begin();
        for (ParallaxMap map : maps) {
            map.draw(batch);
        }
        batch.end();
    }
}