package com.edlplan.framework.game.g2d.draw;

import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.utils.advance.LinkedNode;

import java.util.Iterator;

public class DrawGroup implements IDrawObject{

    private DrawObjectManager drawObjectManager;

    public DrawGroup(DrawObjectManager manager) {
        this.drawObjectManager = manager;
    }

    public void setDrawObjectManager(DrawObjectManager drawObjectManager) {
        this.drawObjectManager = drawObjectManager;
    }

    @SuppressWarnings("unchecked")
    public <T extends DrawObjectManager> T getDrawObjectManager() {
        return (T) drawObjectManager;
    }

    @Override
    public void draw(BaseCanvas canvas) {
        if (drawObjectManager != null) {
            drawObjectManager.drawAll(canvas);
        }
    }

    public interface DrawObjectManager {

        Iterator<IDrawObject> iterator();

        default void drawAll(BaseCanvas canvas) {
            Iterator<IDrawObject> objectIterator = iterator();
            while (objectIterator.hasNext()) {
                objectIterator.next().draw(canvas);
            }
        }

    }

    public static class LinkedDrawObjectManager implements DrawObjectManager {

        LinkedNode<IDrawObject> first;

        LinkedNode<IDrawObject> last;

        public LinkedDrawObjectManager() {
            first = new LinkedNode<>();
            last = new LinkedNode<>();
            first.insertToNext(last);
        }

        @Override
        public Iterator<IDrawObject> iterator() {
            return new Iterator<IDrawObject>() {

                LinkedNode<IDrawObject> cur = first.next;

                @Override
                public boolean hasNext() {
                    return cur != last;
                }

                @Override
                public IDrawObject next() {
                    IDrawObject object = cur.value;
                    cur = cur.next;
                    return object;
                }
            };
        }

        @Override
        public void drawAll(BaseCanvas canvas) {
            LinkedNode<IDrawObject> cur = first.next;
            while (cur != last) {
                cur.value.draw(canvas);
                cur = cur.next;
            }
        }
    }

}
