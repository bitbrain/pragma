package de.bitbrain.pragma.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;
import de.bitbrain.pragma.events.ShowPageEvent;

public class PageCounter extends Actor implements GameEventListener<ShowPageEvent> {

    private Label label;
    private final int maxPages;
    private int currentPages;
    private Sprite sprite;

    public PageCounter(int maxPages) {
        this.maxPages = maxPages;
        label = new Label("0/0", Styles.DIALOG_TEXT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (currentPages == 0) {
            return;
        }
        if (sprite == null) {
            sprite = new Sprite(SharedAssetManager.getInstance().get(Assets.Textures.PAGE, Texture.class));
            sprite.setColor(Colors.TEXT_SPEECH);
        }
        sprite.setPosition(getX(), getY());
        sprite.setSize(label.getPrefHeight(), label.getPrefHeight());
        label.setText(currentPages + " / " + maxPages);
        label.setPosition(label.getPrefHeight() + 10f + getX(), getY());
        label.draw(batch, parentAlpha);
        sprite.draw(batch, parentAlpha);
    }

    @Override
    public void onEvent(ShowPageEvent event) {
        currentPages++;
    }
}
