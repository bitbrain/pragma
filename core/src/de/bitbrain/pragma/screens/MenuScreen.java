package de.bitbrain.pragma.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.ui.Styles;

public class MenuScreen extends AbstractScreen<BrainGdxGame> {

    private boolean exiting;

    private GameContext context;

    public MenuScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        setBackgroundColor(Colors.BACKGROUND);

        context.getAudioManager().fadeInMusic(Assets.Musics.MAIN_MENU, 7f);
        this.context = context;
        context.getScreenTransitions().in(1.5f);

        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setLutIntensity(0.7f);
        vignette.setIntensity(0.9f);
        vignette.setSaturationMul(1.1f);
        context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(vignette);

        Table layout = new Table();
        layout.setFillParent(true);
        Label logo = new Label("pragma", Styles.LABEL_LOGO);
        layout.add(logo).row();

        Label pressAnyButton = new Label("press any key", Styles.DIALOG_TEXT);
        layout.add(pressAnyButton).padTop(Gdx.graphics.getHeight() / 6f).row();

        Label credits = new Label("coding @ bitbrain - audio @ k0stnix", Styles.LABEL_CREDITS);
        credits.getColor().a = 0f;
        layout.add(credits).padTop(Gdx.graphics.getHeight() / 10f);
        context.getStage().addActor(layout);

        pressAnyButton.getColor().a = 0f;
        Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f).delay(2f)
                .ease(TweenEquations.easeInCubic)
                .start(context.getTweenManager());

        Tween.to(credits, ActorTween.ALPHA, 3f).target(0.2f).delay(3f)
                .ease(TweenEquations.easeInCubic)
                .start(context.getTweenManager());

        Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f).delay(3f)
                .target(1f)
                .ease(TweenEquations.easeInCubic)
                .repeatYoyo(Tween.INFINITY, 0f)
                .start(context.getTweenManager());

        logo.getColor().a = 0f;
        Tween.to(logo, ActorTween.ALPHA, 3f).target(1f).delay(0f)
                .ease(TweenEquations.easeInCubic)
                .start(context.getTweenManager());

        Bloom bloom = new Bloom(Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 6);
        bloom.setBlurPasses(40);
        bloom.setBloomIntesity(0.7f);
        context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(bloom);

        GameObject background = context.getGameWorld().addObject();
        background.setDimensions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setType("background-01");
        context.getRenderManager().register("background-01", new SpriteRenderer(Assets.Textures.MOUNTAINS_01));
    }

    @Override
    protected void onUpdate(float delta) {
        if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
            exiting = true;
            SharedAssetManager.getInstance().get(Assets.Sounds.BUTTON, Sound.class).play();
            context.getScreenTransitions().out(new StoryScreen(getGame()), 1f);
        }
    }
}
