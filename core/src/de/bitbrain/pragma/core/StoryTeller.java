package de.bitbrain.pragma.core;

import java.util.ArrayList;
import java.util.List;

public class StoryTeller {

    private List<String> texts;

    public StoryTeller() {
        texts = new ArrayList<String>();
        initTexts();
    }

    public String getNextStoryPoint() {
        return texts.remove(0);
    }

    public boolean hasNextStoryPoint() {
        return !texts.isEmpty();
    }

    private void initTexts() {
        texts.add("John promised his parents to be home early. On his way home he came across a lonely parking lot.");
        texts.add("His grandma told him stories about this place. \nA dark forest, abandoned for many years.");
        texts.add("About fifty years ago a group of young students went camping here.\n\nThey were never seen again.");
        texts.add("As John approached the wooden fence he noticed something odd...");
        texts.add("There was a car!\nEngines still running.");
        texts.add("Curiosity took over and he decided to approach the vehicle...");
        texts.add("This day would change his life forever.");
    }
}