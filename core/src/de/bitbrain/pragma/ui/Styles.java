package de.bitbrain.pragma.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.bitbrain.braingdx.graphics.BitmapFontBaker;
import de.bitbrain.pragma.Assets;

public final class Styles {

    public static final Label.LabelStyle DIALOG_TEXT = new Label.LabelStyle();

    public static void init() {
        DIALOG_TEXT.fontColor = Color.WHITE;
        DIALOG_TEXT.font = BitmapFontBaker.bake(Assets.Fonts.TEXT, 30);
    }
}
