package de.bitbrain.pragma;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.pragma.screens.IngameScreen;

public class PragmaGame extends BrainGdxGame {
    @Override
    protected GameAssetLoader getAssetLoader() {
        return new SmartAssetLoader(Assets.class);
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
        Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        //Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        return new IngameScreen(this);
    }
}
