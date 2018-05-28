package de.bitbrain.pragma;

public interface Assets {

    interface TiledMaps {
        String INTRO = "maps/world.tmx";
    }

    interface Textures {
        String PLAYER_TILESET = "textures/player.png";
        String KALMAG_TILESET = "textures/kalmag.png";
        String BITBRAIN_LOGO = "textures/bitbrain.png";
        String PAGE = "textures/page.png";
        String PAGE_NINEPATCH = "textures/page.9.png";
        String MOUNTAINS_01 = "textures/mountains-01.png";
    }

    interface Fonts {
        String TEXT = "fonts/Pixellari.ttf";
    }

    interface Sounds {
        String BITBRAIN = "sounds/bitbrain.ogg";
        String ENGINE_RUNNING = "sounds/engine-running.ogg";
        String CREATURE = "sounds/creature.ogg";
        String FOOTSTEP = "sounds/step.ogg";
    }

    interface Musics {
        String SOUNDSCAPE = "music/soundscape.ogg";
        String ESCAPE = "music/escape.ogg";
    }

    interface Particles {
        String AURA = "particles/aura.p";
    }
}
