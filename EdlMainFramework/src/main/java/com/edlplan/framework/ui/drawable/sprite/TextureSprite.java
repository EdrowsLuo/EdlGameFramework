package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.MContext;
import com.edlplan.framework.math.Anchor;
import com.edlplan.framework.math.RectF;

public class TextureSprite extends BaseRectTextureSprite<TextureSpriteShader> {
    public TextureSprite(MContext c) {
        super(c);
    }

    public void setAreaFitTexture(RectF rect) {
        float w = rect.getWidth() / getTexture().getWidth();
        float h = rect.getHeight() / getTexture().getHeight();
        if (w > h) {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getHeight() * getTexture().getWidth() / getTexture().getHeight(), rect.getHeight()));
        } else {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getWidth(), rect.getWidth() * getTexture().getHeight() / getTexture().getWidth()));
        }
    }

    public void setAreaFillTexture(RectF rect) {
        float w = rect.getWidth() / getTexture().getWidth();
        float h = rect.getHeight() / getTexture().getHeight();
        if (w < h) {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getHeight() * getTexture().getWidth() / getTexture().getHeight(), rect.getHeight()));
        } else {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getWidth(), rect.getWidth() * getTexture().getHeight() / getTexture().getWidth()));
        }
    }

    @Override
    protected TextureSpriteShader getShader() {
        return TextureSpriteShader.get();
    }
}
