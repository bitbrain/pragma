package de.bitbrain.pragma.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.Config;
import de.bitbrain.pragma.core.StoryTeller;
import de.bitbrain.pragma.ui.Styles;

public class EscapeSuccessfulScreen extends AbstractScreen<BrainGdxGame> {

    private String[] STORY = {
        "John didn't look back. He kept running.",
        "With tears in his eyes he arrived back home when suddenly...",
        "MERLIN! The dog sat in front of the door, healthy and full of mud!",
        "In the distance a strange sound.\n\nIt came closer. Much closer."
    };

    private Label label, action;

    private StoryTeller teller;

    private boolean aborted = false;

    private GameContext context;

    public EscapeSuccessfulScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {

        setBackgroundColor(Colors.BACKGROUND);
        this.context = context;
        teller = new StoryTeller(STORY);

        context.getScreenTransitions().in(2f);

        Table layout = new Table();
        layout.setFillParent(true);
        SharedAssetManager.getInstance().get(Assets.Musics.SOUNDSCAPE, Music.class).stop();
        SharedAssetManager.getInstance().get(Assets.Musics.DANGER, Music.class).stop();
        SharedAssetManager.getInstance().get(Assets.Musics.DANGER_EXTREME, Music.class).stop();
        context.getAudioManager().fadeInMusic(Assets.Musics.STORY_MENU, 3f);
        label = new Label(teller.getNextStoryPoint(), Styles.DIALOG_TEXT);
        label.setWrap(true);
        label.setAlignment(Align.center);
        layout.center().add(label).width(600f).padBottom(200f).row();
        action = new Label("Press any KEY to continue", Styles.DIALOG_TEXT);
        action.setColor(Color.WHITE);
        action.getColor().a = 0.5f;
        Tween.to(action, ActorTween.ALPHA, 1f).target(0.2f)
                .ease(TweenEquations.easeInOutCubic)
                .repeatYoyo(Tween.INFINITY, 0f)
                .start(context.getTweenManager());
        layout.center().add(action).row();

        context.getStage().addActor(layout);
    }

    @Override
    protected void onUpdate(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !aborted) {
            context.getScreenTransitions().out(new IngameScreen(getGame()), 2f);
            SharedAssetManager.getInstance().get(Assets.Sounds.BUTTON, Sound.class).play();
            aborted = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) && !aborted) {
            if (teller.hasNextStoryPoint()) {
                SharedAssetManager.getInstance().get(Assets.Sounds.BUTTON, Sound.class).play();
                context.getTweenManager().killTarget(label);
                Tween.to(label, ActorTween.ALPHA, 0.5f)
                        .target(0f)
                        .ease(TweenEquations.easeOutQuad)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                label.setText(teller.getNextStoryPoint());
                                Tween.to(label, ActorTween.ALPHA, 1f)
                                        .target(1f)
                                        .ease(TweenEquations.easeInOutQuad)
                                        .start(context.getTweenManager());
                            }
                        })
                        .setCallbackTriggers(TweenCallback.COMPLETE)
                        .start(context.getTweenManager());
            } else if (!aborted) {
                aborted = true;
                SharedAssetManager.getInstance().get(Assets.Musics.STORY_MENU, Music.class).stop();
                context.getScreenTransitions().out(new MenuScreen(getGame()), 2f);
            }
        }
    }
}
