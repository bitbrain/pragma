package de.bitbrain.pragma;

public interface Assets {

    interface TiledMaps {
        String INTRO = "maps/world.tmx";
    }

    interface Textures {
        String DOG_TILESET = "textures/dog.png";
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
        String CREATE_2 = "sounds/creature_2.ogg";
        String FOOTSTEP = "sounds/step.ogg";
        String CREATURE_STEP = "sounds/step_1.ogg";
        String BARK = "sounds/bark.ogg";
        String WHINING = "sounds/whining.ogg";
        String BUTTON = "sounds/button.ogg";
        String PAPER_OPEN = "sounds/paper_open_1.ogg";
        String PAPER_CLOSE = "sounds/paper_close.ogg";
    }

    interface Musics {
        String SOUNDSCAPE = "music/soundscape.ogg";
        String ESCAPE = "music/escape.ogg";
        String STORY_MENU = "music/storyscreen.ogg";
        String MAIN_MENU = "music/mainmenu.ogg";
        String DANGER = "music/danger.ogg";
        String DANGER_EXTREME = "music/great_danger.ogg";

    }

    interface Particles {
        String AURA = "particles/aura.p";
    }
}
