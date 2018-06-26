package de.bitbrain.pragma.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.events.ShowPageEvent;

public class PageHandler extends InputAdapter implements GameEventListener<ShowPageEvent> {

    private final GameContext context;
    private Actor actor;

    public PageHandler(GameContext context) {
        this.context = context;
    }


    @Override
    public void onEvent(ShowPageEvent event) {
        Label text = new Label(event.getCaption() + "\n- - -\n" + event.getText(), Styles.PAGE);
        text.setWrap(true);
        text.setAlignment(Align.center);
        if (actor != null) {
            context.getStage().getActors().removeValue(actor, true);
        }
        Table layout = new Table();
        actor = layout;
        layout.setFillParent(true);
        layout.center().add(text).width(Gdx.graphics.getWidth() / 5f);
        context.getStage().addActor(layout);
        SharedAssetManager.getInstance().get(Assets.Sounds.PAPER_OPEN, Sound.class).play();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (actor != null && (keycode == Input.Keys.X || keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE)) {
            context.getStage().getActors().removeValue(actor, true);
            actor = null;
            SharedAssetManager.getInstance().get(Assets.Sounds.PAPER_CLOSE, Sound.class).play();
        }
        return true;
    }
}
