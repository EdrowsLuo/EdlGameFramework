package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.BuildConfig;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;
import com.edlplan.framework.timing.Schedule;
import com.edlplan.framework.utils.FactoryV1;
import com.edlplan.framework.utils.advance.LinkedNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayingLayer {

    private Schedule schedule = new Schedule();

    private LinkedNode<PlayingSprite> first, end;

    private LinkedNode<PlayingSprite>[] ary;

    private FactoryV1<PlayingSprite, StoryboardSprite> playingSpriteFactory;

    private boolean preLoad = false;

    public PlayingLayer(FactoryV1<PlayingSprite, StoryboardSprite> playingSpriteFactory) {
        this.playingSpriteFactory = playingSpriteFactory;
        first = new LinkedNode<>();
        end = new LinkedNode<>();
        first.insertToNext(end);
    }

    public void setPreLoad(boolean preLoad) {
        this.preLoad = preLoad;
    }

    public boolean isPreLoad() {
        return preLoad;
    }

    @SuppressWarnings("unchecked")
    public void addSprites(Collection<StoryboardSprite> sprites) {
        ary = new LinkedNode[sprites.size()];
        int idx = 0;
        List<Schedule.Task> tasks = new ArrayList<>();
        for (StoryboardSprite sprite : sprites) {
            //if (BuildConfig.DEBUG) System.out.println("Schedule " + idx);
            tasks.add(new Schedule.Task(sprite.startTime(), () -> addSpriteToScene(sprite)));
            tasks.add(new Schedule.Task(sprite.endTime(), () -> removeSpriteFromScene(sprite)));
            idx++;
        }
        schedule.setTasks(tasks);

        if (preLoad) {
            int i = 0;
            for (StoryboardSprite sprite : sprites) {
                PlayingSprite playingSprite = playingSpriteFactory.create(sprite);
                playingSprite.load(sprite);
                ary[i] = new LinkedNode<>(playingSprite);
                i++;
            }
        }

        //System.out.println(schedule.getTasks());
    }

    protected void addSpriteToScene(StoryboardSprite sprite) {
        //if (BuildConfig.DEBUG) {
        //    System.out.println("Add sprite " + sprite);
        //}
        if (preLoad) {
            ary[sprite.depth].value.onAddedToScene();
        } else {
            PlayingSprite playingSprite = playingSpriteFactory.create(sprite);
            playingSprite.load(sprite);
            playingSprite.onAddedToScene();
            end.insertToPrevious(ary[sprite.depth] = new LinkedNode<>(playingSprite));
        }
        //if (BuildConfig.DEBUG) {
        //    System.out.println("Add sprite " + playingSprite);
        //}
    }

    protected void removeSpriteFromScene(StoryboardSprite sprite) {
        if (ary[sprite.depth] != null) {
            ary[sprite.depth].removeFromList();
            ary[sprite.depth].value.onRemoveFromScene();
            ary[sprite.depth] = null;
        }
    }

    public void update(double time) {
        schedule.update(time);
        for (LinkedNode<PlayingSprite> s = first.next; s != end; s = s.next) {
            s.value.update(time);
        }
    }

}
