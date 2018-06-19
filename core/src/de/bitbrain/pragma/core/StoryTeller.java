package de.bitbrain.pragma.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoryTeller {

    private List<String> texts;

    public StoryTeller(String [] textBits) {
        texts = new ArrayList<String>(Arrays.<String>asList(textBits));
    }

    public String getNextStoryPoint() {
        return texts.remove(0);
    }

    public boolean hasNextStoryPoint() {
        return !texts.isEmpty();
    }
}