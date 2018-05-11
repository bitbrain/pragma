package de.bitbrain.pragma.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.BitmapFontBaker;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.pragma.Assets;
import de.bitbrain.pragma.Colors;

public final class Styles {

    public static final Label.LabelStyle DIALOG_TEXT = new Label.LabelStyle();

    public static void init() {
        DIALOG_TEXT.fontColor = Colors.TEXT_SPEECH;
        DIALOG_TEXT.font = bake(Assets.Fonts.TEXT, 45);
    }

    public static BitmapFont bake(String fontPath, int size) {
        FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;
        param.mono = true;
        param.size = size;
        return generator.generateFont(param);
    }
}
