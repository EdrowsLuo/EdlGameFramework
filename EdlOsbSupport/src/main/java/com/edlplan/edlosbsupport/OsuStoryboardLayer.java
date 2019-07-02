package com.edlplan.edlosbsupport;

import com.edlplan.edlosbsupport.elements.IStoryboardElement;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;

import java.io.Serializable;
import java.util.ArrayList;

public class OsuStoryboardLayer implements Serializable {

    public StoryboardSprite.Layer layer;

    public ArrayList<IStoryboardElement> elements = new ArrayList<>();

    public OsuStoryboardLayer() {

    }

    public OsuStoryboardLayer(StoryboardSprite.Layer layer) {
        this.layer = layer;
    }

    public void clear() {
        for (IStoryboardElement element : elements) {
            element.clear();
        }
        elements.clear();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(layer).append("]");
        for (IStoryboardElement element : elements) {
            stringBuilder.append('\n').append(element);
        }
        return stringBuilder.toString();
    }
}
