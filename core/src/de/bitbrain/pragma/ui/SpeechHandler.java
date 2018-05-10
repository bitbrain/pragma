package de.bitbrain.pragma.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.pragma.events.SayEvent;

public class SpeechHandler implements GameEventListener<SayEvent> {

    private final Stage stage;

    private Actor actor;

    public SpeechHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onEvent(SayEvent event) {
        if (actor != null) {
            SharedTweenManager.getInstance().killTarget(actor);
            stage.getActors().removeValue(actor, true);
        }
        actor = createDialog(event);
        Table layout = new Table();
        layout.setFillParent(true);
        layout.bottom().add(actor).padBottom(Gdx.graphics.getHeight() / 5f);
        stage.addActor(layout);
        Tween.to(actor, ActorTween.ALPHA, 1f).delay(3f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        stage.getActors().removeValue(actor, false);
                    }
                })
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .start(SharedTweenManager.getInstance());
    }

    private Actor createDialog(SayEvent event) {
        return new Label(event.getText(), Styles.DIALOG_TEXT);
    }
}
