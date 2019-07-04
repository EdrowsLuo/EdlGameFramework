package com.edlplan.newosbplayer;

import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edlplan.framework.utils.advance.LinkedNode;

public class DepthOrderRenderEngine {

    public LinkedNode<EGFStoryboardSprite> first,end;

    public DepthOrderRenderEngine() {
        first = new LinkedNode<>();
        end = new LinkedNode<>();
        first.insertToNext(end);
    }

    public void add(EGFStoryboardSprite sprite) {
        for (LinkedNode<EGFStoryboardSprite> s = end.pre; s != first; s = s.pre) {
            if (s.value.sprite.depth < sprite.sprite.depth) {
                s.insertToNext(new LinkedNode<>(sprite));
                return;
            }
        }
        first.insertToNext(new LinkedNode<>(sprite));
    }

    public void remove(EGFStoryboardSprite sprite) {
        for (LinkedNode<EGFStoryboardSprite> s = first.next; s != end; s = s.next) {
            if (s.value == sprite) {
                s.removeFromList();
                break;
            }
        }
    }

    public void draw(BaseCanvas canvas) {
        TextureQuadBatch batch = TextureQuadBatch.getDefaultBatch();
        for (LinkedNode<EGFStoryboardSprite> s = first.next; s != end; s = s.next) {
            if (s.value.textureQuad.alpha.value < 0.001) {
                continue;
            }
            canvas.getBlendSetting().setBlendType(s.value.blendMode.value ? BlendType.Additive : BlendType.Normal);
            batch.add(s.value.textureQuad);
        }
    }


}
