package com.edlplan.framework.graphics.opengl.external;

import com.edlplan.framework.graphics.opengl.shader.GLProgram;
import com.edlplan.framework.graphics.opengl.shader.advance.Texture2DShader;
import com.edlplan.framework.utils.Lazy;

public class ExternalTexture2DShader extends Texture2DShader {

    public static final Lazy<ExternalTexture2DShader> DEFAULT;

    static {
        DEFAULT = Lazy.create(() -> new ExternalTexture2DShader(
                GLProgram.createProgram(
                        "" +
                                "uniform mat4 u_MVPMatrix;\n" +
                                "uniform mat4 u_MaskMatrix;\n" +
                                "uniform float u_FinalAlpha;\n" +
                                "uniform vec4 u_AccentColor;\n" +
                                "\n" +
                                "attribute vec4 a_PositionAndCoord;\n" +
                                "attribute vec4 a_VaryingColor;\n" +
                                "\n" +
                                "varying vec4 f_VaryingColor;\n" +
                                "varying vec2 f_TextureCoord;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    f_TextureCoord = a_PositionAndCoord.ba;\n" +
                                "    f_VaryingColor = a_VaryingColor * u_FinalAlpha;\n" +
                                "    gl_Position = u_MVPMatrix * vec4(a_PositionAndCoord.rg, 1.0, 1.0);\n" +
                                "}",
                        "" +
                                "#extension GL_OES_EGL_image_external : require\n" +
                                "precision mediump float;\n" +
                                "\n" +
                                "uniform samplerExternalOES u_Texture;\n" +
                                "varying lowp vec4 f_VaryingColor;\n" +
                                "varying vec2 f_TextureCoord;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    gl_FragColor = f_VaryingColor * texture2D(u_Texture, f_TextureCoord);\n" +
                                "}"
                )
        ));
    }

    protected ExternalTexture2DShader(GLProgram program) {
        super(program);
    }

}
