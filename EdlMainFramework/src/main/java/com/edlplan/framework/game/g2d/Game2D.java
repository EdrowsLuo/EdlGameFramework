package com.edlplan.framework.game.g2d;

public abstract class Game2D {

    public abstract <T> T createObjectByType(Class<T> klass);

    /**
     * 获取
     *
     * @param tag
     * @param <T>
     * @return
     */
    public abstract <T> T getGlobalObjectByTag(String tag);

    public abstract int getGlobalObjectIdByTag(String tag);

    public abstract <T> T getGlobalObjectById(int id);

}
