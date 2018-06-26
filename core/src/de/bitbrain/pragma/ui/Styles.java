package de.bitbrain.pragma.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
    public static final Label.LabelStyle PAGE = new Label.LabelStyle();
    public static final Label.LabelStyle LABEL_LOGO = new Label.LabelStyle();
    public static final Label.LabelStyle LABEL_GAME_OVER = new Label.LabelStyle();
    public static final Label.LabelStyle LABEL_CREDITS = new Label.LabelStyle();

    public static void init() {
        DIALOG_TEXT.fontColor = Colors.TEXT_SPEECH;
        DIALOG_TEXT.font = bake(Assets.Fonts.TEXT, 45);

        PAGE.fontColor = Colors.TEXT_PAGE;
        NinePatch patch = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.PAGE_NINEPATCH, Texture.class), 10);
        patch.scale(8f, 8f);
        PAGE.background = new NinePatchDrawable(patch);
        PAGE.font = bake(Assets.Fonts.TEXT, 28);

        LABEL_GAME_OVER.fontColor = Color.valueOf("ff0042");
        LABEL_GAME_OVER.font = bake(Assets.Fonts.TEXT, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight()) / 1600);
        LABEL_LOGO.fontColor = Color.valueOf("3c00ff");
        LABEL_LOGO.font = bake(Assets.Fonts.TEXT, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight()) / 2300);
        LABEL_CREDITS.fontColor = Color.valueOf("3c00ff");
        LABEL_CREDITS.font = bake(Assets.Fonts.TEXT, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight()) / 9300);
    }

    public static BitmapFont bake(String fontPath, int size) {
        FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;
        param.mono = false;
        param.size = size;
        return generator.generateFont(param);
    }
}
