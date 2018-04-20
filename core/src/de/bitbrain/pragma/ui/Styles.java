package de.bitbrain.pragma.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.BitmapFontBaker;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.pragma.Assets;

public final class Styles {

    public static final Label.LabelStyle DIALOG_TEXT = new Label.LabelStyle();

    public static void init() {
        DIALOG_TEXT.fontColor = Color.valueOf("#e3826c");
        DIALOG_TEXT.font = BitmapFontBaker.bake(Assets.Fonts.TEXT, 35);
        DIALOG_TEXT.background = new NinePatchDrawable(GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.DIALOG_NINEPATCH, Texture.class), 32));
    }
}
