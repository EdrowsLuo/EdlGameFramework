package com.edlplan.framework.graphics.opengl.external;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.graphics.opengl.shader.advance.Texture2DShader;

public class ExternalGLTexture extends GLTexture {

    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
        buildRawQuad();
    }

    @Override
    public Texture2DShader getDefaultTextureShader() {
        return ExternalTexture2DShader.DEFAULT.get();
    }

    public static ExternalGLTexture createOESTexture(int width, int height) {
        int[] textureId = new int[1];
        GLES20.glGenTextures(1, textureId, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        ExternalGLTexture oesglTexture = new ExternalGLTexture();
        oesglTexture.glTextureType = GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
        oesglTexture.width = width;
        oesglTexture.height = height;
        oesglTexture.textureId = textureId[0];
        oesglTexture.glWidth = 1;
        oesglTexture.glHeight = 1;
        oesglTexture.endCreate();
        return oesglTexture;
    }


}
