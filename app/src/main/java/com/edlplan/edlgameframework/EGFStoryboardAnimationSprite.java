package com.edlplan.edlgameframework;

import com.edlplan.edlosbsupport.elements.StoryboardAnimationSprite;
import com.edlplan.framework.graphics.opengl.batch.v2.object.FlippableTextureQuad;

public class EGFStoryboardAnimationSprite extends EGFStoryboardSprite {

    public EGFStoryboardAnimationSprite(OsbContext context) {
        super(context);
    }

    @Override
    protected void onLoad() {
        textureQuad = new FlippableTextureQuad();
        textureQuad.setTextureAndSize(context.texturePool.getTexture(((StoryboardAnimationSprite) sprite).buildPath(0)));
        textureQuad.position.x.value = sprite.startX;
        textureQuad.position.y.value = sprite.startY;
        textureQuad.anchor = sprite.origin.value;
    }

}
