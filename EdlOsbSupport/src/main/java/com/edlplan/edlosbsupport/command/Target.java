package com.edlplan.edlosbsupport.command;

public enum Target {
    Alpha(SpriteCommand.TargetType.FLOAT),
    X(SpriteCommand.TargetType.FLOAT),
    Y(SpriteCommand.TargetType.FLOAT),
    ScaleX(SpriteCommand.TargetType.FLOAT),
    ScaleY(SpriteCommand.TargetType.FLOAT),
    Rotation(SpriteCommand.TargetType.FLOAT),
    Color(SpriteCommand.TargetType.COLOR4),
    BlendingMode(SpriteCommand.TargetType.BOOLEAN),
    FlipH(SpriteCommand.TargetType.BOOLEAN),
    FlipV(SpriteCommand.TargetType.BOOLEAN);

    public final SpriteCommand.TargetType targetType;

    Target(SpriteCommand.TargetType targetType) {
        this.targetType = targetType;
    }

}
