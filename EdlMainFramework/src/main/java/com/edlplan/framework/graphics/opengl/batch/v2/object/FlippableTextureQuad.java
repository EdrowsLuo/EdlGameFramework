package com.edlplan.framework.graphics.opengl.batch.v2.object;

import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.math.Anchor;
import com.edlplan.framework.math.Color4;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.BooleanRef;
import com.edlplan.framework.utils.FloatRef;
import com.edlplan.framework.utils.Vec2Ref;

public class FlippableTextureQuad extends ATextureQuad {

    public BooleanRef flipH = new BooleanRef(false);

    public BooleanRef flipV = new BooleanRef(false);

    public Vec2 size = new Vec2();

    public Vec2Ref position = new Vec2Ref();

    public Anchor anchor = Anchor.Center;

    public Vec2Ref scale;

    public FloatRef rotation;

    public FloatRef alpha = new FloatRef(1);

    public Color4 accentColor;

    public float u1, v1, u2, v2;

    public FlippableTextureQuad enableScale() {
        if (scale == null) {
            scale = new Vec2Ref();
            scale.x.value = scale.y.value = 1;
        }
        return this;
    }

    public FlippableTextureQuad enableColor() {
        if (accentColor == null) {
            accentColor = Color4.White.copyNew();
        }
        return this;
    }

    public FlippableTextureQuad syncColor(Color4 color) {
        accentColor = color;
        return this;
    }

    public FlippableTextureQuad syncAlpha(FloatRef ref) {
        this.alpha = ref;
        return this;
    }

    public FlippableTextureQuad enableRotation() {
        if (rotation == null) {
            rotation = new FloatRef();
        }
        return this;
    }

    public FlippableTextureQuad syncRotation(FloatRef ref) {
        this.rotation = ref;
        return this;
    }

    public void setTextureAndSize(AbstractTexture texture) {
        this.texture = texture;
        size.set(texture.getWidth(), texture.getHeight());
        RectF rectF = texture.toTextureRect(0, 0, texture.getWidth(), texture.getHeight());
        u1 = rectF.getLeft();
        u2 = rectF.getRight();
        v1 = rectF.getTop();
        v2 = rectF.getBottom();
    }

    public void setBaseWidth(float width) {
        size.set(width, width * (size.y / size.x));
    }

    public void setBaseHeight(float height) {
        size.set(height * (size.x / size.y), height);
    }

    @Override
    public void write(float[] ary, int offset) {

        float u1;
        float v1;
        float u2;
        float v2;

        if (flipH.value) {
            u1 = this.u2;
            u2 = this.u1;
        } else {
            u1 = this.u1;
            u2 = this.u2;
        }

        if (flipV.value) {
            v1 = this.v2;
            v2 = this.v1;
        } else {
            v1 = this.v1;
            v2 = this.v2;
        }

        float l = -size.x * anchor.x();
        float r = size.x + l;
        float t = -size.y * anchor.y();
        float b = size.y + t;
        float color = accentColor == null ? Color4.getPackedAlphaBit(alpha.value) : accentColor.toFloatBitABGR(alpha.value);

        if (scale != null) {
            l *= scale.x.value;
            r *= scale.x.value;
            t *= scale.y.value;
            b *= scale.y.value;
        }

        if (rotation == null) {
            l += position.x.value;
            r += position.x.value;
            t += position.y.value;
            b += position.y.value;
            ary[offset++] = l;
            ary[offset++] = t;
            ary[offset++] = u1;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = r;
            ary[offset++] = t;
            ary[offset++] = u2;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = l;
            ary[offset++] = b;
            ary[offset++] = u1;
            ary[offset++] = v2;
            ary[offset++] = color;


            ary[offset++] = r;
            ary[offset++] = b;
            ary[offset++] = u2;
            ary[offset++] = v2;
            ary[offset] = color;
        } else {
            final float s = (float) Math.sin(rotation.value);
            final float c = (float) Math.cos(rotation.value);
            final float x = position.x.value, y = position.y.value;
            ary[offset++] = l * c - t * s + x;
            ary[offset++] = l * s + t * c + y;
            ary[offset++] = u1;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = r * c - t * s + x;
            ary[offset++] = r * s + t * c + y;
            ary[offset++] = u2;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = l * c - b * s + x;
            ary[offset++] = l * s + b * c + y;
            ary[offset++] = u1;
            ary[offset++] = v2;
            ary[offset++] = color;

            ary[offset++] = r * c - b * s + x;
            ary[offset++] = r * s + b * c + y;
            ary[offset++] = u2;
            ary[offset++] = v2;
            ary[offset] = color;
        }
    }

}
