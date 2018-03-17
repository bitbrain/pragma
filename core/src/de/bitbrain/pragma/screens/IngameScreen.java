package de.bitbrain.pragma.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.pragma.Colors;


public class IngameScreen extends AbstractScreen<BrainGdxGame> {

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        setBackgroundColor(Colors.BACKGROUND);
        context.getScreenTransitions().in(5.5f);

        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setLutIntensity(0.5f);
        vignette.setIntensity(0.5f);
        vignette.setSaturationMul(0.8f);
        context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(vignette);
    }

    @Override
    protected void onUpdate(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}
