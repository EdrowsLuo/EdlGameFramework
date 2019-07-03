package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.command.ACommand;
import com.edlplan.edlosbsupport.command.SpriteCommand;
import com.edlplan.edlosbsupport.command.Target;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;

public abstract class PlayingSprite {

    private CommandHandleTimeline[] timelines = new CommandHandleTimeline[Target.values().length];

    public StoryboardSprite sprite;

    public abstract void onAddedToScene();

    public abstract void onRemoveFromScene();

    public abstract CommandHandleTimeline createByTarget(Target target);

    private CommandHandleTimeline getTimeline(Target target) {
        int idx = target.ordinal();
        if (timelines[idx]==null) timelines[idx] = createByTarget(target);
        return timelines[idx];
    }

    /**
     * 在这里用sprite数据初始化平台相关的绘制对象
     */
    protected abstract void onLoad();

    @SuppressWarnings("unchecked")
    public final void load(StoryboardSprite storyboardSprite) {
        this.sprite = storyboardSprite;
        onLoad();
        SpriteCommand spriteCommand;
        for (ACommand command : sprite.rootGroup.commands) {
            spriteCommand = (SpriteCommand) command;
            getTimeline(spriteCommand.target).addCommand(spriteCommand);
        }
        update(sprite.startTime());
    }

    public void update(double time) {
        for (CommandHandleTimeline timeline : timelines) {
            if (timeline != null) {
                timeline.update(time);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Sprite " + sprite).append('\n');
        int i = 0;
        for (CommandHandleTimeline l : timelines) {
            stringBuilder.append("\n\nTarget ").append(Target.values()[i]).append('\n');
            stringBuilder.append(l);
            i++;
        }
        return stringBuilder.toString();
    }
}
