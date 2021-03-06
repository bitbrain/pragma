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
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.core.StoryTeller;
import de.bitbrain.pragma.ui.Styles;

public class StoryScreen extends AbstractScreen<BrainGdxGame> {

    private String[] STORY = {
            "John walked his Dog Merlin and decided to go a different route this time.\nSuddently he came across a lonely parking lot.",
            "His grandma told him stories about this place. \nA dark forest, abandoned for many years.",
            "About fifty years ago a group of young students went camping here.\n\nThey were never seen again.",
            "As John approached the wooden fence he noticed something odd...",
            "There was a car!\nEngines still running.",
            "Curiosity took over and he decided to approach the vehicle...",
            "This day would change his life forever."
    };

    private Label label, action;

    private StoryTeller teller;

    private boolean aborted = false;

    private GameContext context;

    public StoryScreen(BrainGdxGame game) {
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

        // Creepy stuff
        Music soundscape = SharedAssetManager.getInstance().get(Assets.Musics.SOUNDSCAPE, Music.class);
        soundscape.setLooping(true);
        soundscape.setVolume(0.15f);
        soundscape.play();

        context.getAudioManager().crossFadeMusic(Assets.Musics.MAIN_MENU, Assets.Musics.STORY_MENU, 6f);
        label = new Label(teller.getNextStoryPoint(), Styles.DIALOG_TEXT);
        label.setWrap(true);
        label.setAlignment(Align.center);
        layout.center().add(label).width(Gdx.graphics.getWidth() / 3f).padBottom(200f).row();
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
                context.getScreenTransitions().out(new IngameScreen(getGame()), 2f);
            }
        }
    }
}
