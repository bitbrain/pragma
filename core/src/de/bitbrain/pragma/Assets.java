package de.bitbrain.pragma;

public interface Assets {

    interface TiledMaps {
        String INTRO = "maps/world.tmx";
    }

    interface Textures {
        String PLAYER_TILESET = "textures/player.png";
        String DIALOG_NINEPATCH = "textures/dialog.9.png";
        String BITBRAIN_LOGO = "textures/bitbrain.png";

        String MOUNTAIN_01 = "textures/mountain-1.png";
    }

    interface Fonts {
        String TEXT = "fonts/Pixellari.ttf";
    }

    interface Sounds {
        String BITBRAIN = "sounds/bitbrain.ogg";
        String ENGINE_RUNNING = "sounds/engine-running.ogg";
    }
}
