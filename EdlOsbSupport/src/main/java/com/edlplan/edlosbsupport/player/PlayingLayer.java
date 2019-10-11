package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.BuildConfig;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;
import com.edlplan.framework.timing.Schedule;
import com.edlplan.framework.utils.FactoryV1;
import com.edlplan.framework.utils.advance.LinkedNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayingLayer {

    private static final ThreadLocal<ExecutorService> executorService = new ThreadLocal<ExecutorService>(){
        @Override
        public ExecutorService get() {
            ExecutorService service = super.get();
            if (service != null) {
                return service;
            } else {
                service = Executors.newFixedThreadPool(5);
                set(service);
                return service;
            }
        }
    };

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
        List<Schedule.Task> tasks = new ArrayList<>();
        for (StoryboardSprite sprite : sprites) {
            tasks.add(new Schedule.Task(sprite.startTime(), () -> addSpriteToScene(sprite)));
            tasks.add(new Schedule.Task(sprite.endTime(), () -> removeSpriteFromScene(sprite)));
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
    }

    protected void addSpriteToScene(StoryboardSprite sprite) {
        if (preLoad) {
            ary[sprite.depth].value.onAddedToScene();
        } else {
            PlayingSprite playingSprite = playingSpriteFactory.create(sprite);
            playingSprite.load(sprite);
            playingSprite.onAddedToScene();
            end.insertToPrevious(ary[sprite.depth] = new LinkedNode<>(playingSprite));
        }
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
