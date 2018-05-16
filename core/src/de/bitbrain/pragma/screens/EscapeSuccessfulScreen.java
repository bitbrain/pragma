package de.bitbrain.pragma.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.ui.Styles;

public class EscapeSuccessfulScreen extends AbstractScreen<BrainGdxGame> {

    public EscapeSuccessfulScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(final GameContext context) {
        setBackgroundColor(Colors.BACKGROUND);
        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setLutIntensity(0.7f);
        vignette.setIntensity(0.9f);
        vignette.setSaturationMul(1.1f);
        context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(vignette);
        context.getScreenTransitions().in(4f);

        Table layout = new Table();
        layout.setFillParent(true);
        Label gameOverLabel = new Label("YOU ESCAPED", Styles.LABEL_GAME_OVER);
        layout.add(gameOverLabel);
        context.getStage().addActor(layout);

        gameOverLabel.getColor().a = 0f;
        Tween.to(gameOverLabel, ActorTween.ALPHA, 5f).target(1f)
                .ease(TweenEquations.easeInCubic)
                .start(context.getTweenManager());

        Bloom bloom = new Bloom(Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 6);
        bloom.setBlurPasses(40);
        bloom.setBloomIntesity(0.7f);
        context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(bloom);

        Tween.to(gameOverLabel, ActorTween.ALPHA, 5f).target(0f).delay(4f)
                .ease(TweenEquations.easeInCubic)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        context.getScreenTransitions().out(new MenuScreen(getGame()), 1f);
                    }
                })
                .start(context.getTweenManager());
    }

    @Override
    protected void onUpdate(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}
