package de.bitbrain.pragma;

public interface Assets {

    interface TiledMaps {
        String INTRO = "maps/world.tmx";
    }

    interface Textures {
        String PLAYER_TILESET = "textures/player.png";
        String BITBRAIN_LOGO = "textures/bitbrain.png";
    }

    interface Fonts {
        String TEXT = "fonts/Pixellari.ttf";
    }

    interface Sounds {
        String BITBRAIN = "sounds/bitbrain.ogg";
        String ENGINE_RUNNING = "sounds/engine-running.ogg";
        String FOOTSTEP = "sounds/step.ogg";
    }

    interface Musics {
        String SOUNDSCAPE = "music/soundscape.ogg";
    }

    interface Particles {
        String AURA = "particles/aura.p";
    }
}
