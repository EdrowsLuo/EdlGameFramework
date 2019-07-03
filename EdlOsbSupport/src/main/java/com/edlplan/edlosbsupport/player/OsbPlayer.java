package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.BuildConfig;
import com.edlplan.edlosbsupport.OsuStoryboard;
import com.edlplan.edlosbsupport.OsuStoryboardLayer;
import com.edlplan.edlosbsupport.elements.IStoryboardElement;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;
import com.edlplan.framework.utils.FactoryV1;

import java.util.ArrayList;
import java.util.Collection;

public class OsbPlayer {

    public enum Mode {
        FastGameplay, //在内存利用上更加优秀的模式，但是会使得反向播放无效
        StrictEditor
    }

    private double currentTime;

    private Mode mode = Mode.FastGameplay;

    private PlayingLayer[] playingLayers = new PlayingLayer[StoryboardSprite.Layer.values().length];

    private FactoryV1<PlayingSprite,StoryboardSprite> spriteFactory;

    private boolean preLoad = false;

    public OsbPlayer(FactoryV1<PlayingSprite, StoryboardSprite> spriteFactory) {
        this.spriteFactory = spriteFactory;
    }

    public void setPreLoad(boolean preLoad) {
        this.preLoad = preLoad;
    }

    public boolean isPreLoad() {
        return preLoad;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void loadStoryboard(OsuStoryboard storyboard) {
        for (OsuStoryboardLayer layer : storyboard.layers) {
            if (layer == null) {
                continue;
            }
            if (BuildConfig.DEBUG) {
                System.out.println("Loading " + layer.layer + " element count " + layer.elements.size());
            }
            int depth = 0;
            ArrayList<StoryboardSprite> sprites = new ArrayList<>();
            for (IStoryboardElement element : layer.elements) {
                ((StoryboardSprite) element).toFlat();
                ((StoryboardSprite) element).sort();
                ((StoryboardSprite) element).depth = depth++;
                sprites.add((StoryboardSprite) element);
            }
            if (BuildConfig.DEBUG) {
                System.out.println("Create playing layer " + layer.layer + " element count " + sprites.size());
            }
            PlayingLayer playingLayer = new PlayingLayer(spriteFactory);
            playingLayer.setPreLoad(preLoad);
            playingLayer.addSprites(sprites);
            if (BuildConfig.DEBUG) {
                System.out.println("Playing layer " + layer.layer + " load sprites, count " + sprites.size());
            }
            playingLayers[layer.layer.ordinal()] = playingLayer;
        }
    }

    public void start(double startTime) {
        currentTime = startTime;
    }

    public void update(double time) {
        currentTime = time;
        for (PlayingLayer layer : playingLayers) {
            if (layer != null) {
                layer.update(time);
            }
        }
    }

}
