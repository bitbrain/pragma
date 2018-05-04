package de.bitbrain.pragma.screens;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.pragma.Colors;

public class MenuScreen extends AbstractScreen<BrainGdxGame> {

    public MenuScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        setBackgroundColor(Colors.BACKGROUND);
        context.getScreenTransitions().in(3.5f);
    }
}
