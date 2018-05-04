package de.bitbrain.pragma;

import com.badlogic.gdx.Gdx;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.pragma.screens.IngameScreen;
import de.bitbrain.pragma.screens.MenuScreen;
import de.bitbrain.pragma.ui.Styles;

public class PragmaGame extends BrainGdxGame {
    @Override
    protected GameAssetLoader getAssetLoader() {
        return new SmartAssetLoader(Assets.class);
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
        Styles.init();
        Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        return new IngameScreen(this);
    }
}
