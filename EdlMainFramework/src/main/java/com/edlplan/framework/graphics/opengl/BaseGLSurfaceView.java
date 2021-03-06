package com.edlplan.framework.graphics.opengl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.edlplan.framework.test.TestStaticData;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

@SuppressLint("ViewConstructor")
public class BaseGLSurfaceView extends GLSurfaceView {
    MainRenderer mRenderer;

    public BaseGLSurfaceView(Context con, MainRenderer r) {
        super(con);
        this.setEGLContextClientVersion(3);
        this.setEGLConfigChooser(new MSAAConfig());
        mRenderer = r;
        mRenderer.setGlVersion(3);
        mRenderer.register(this);
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TestStaticData.touchPosition.set(event.getX(), event.getY());
        mRenderer.onTouch(this, event);
        return true;
    }


    public class MSAAConfig implements EGLConfigChooser {
        @Override
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            EGLConfig cof;
            if ((cof = chooseMSAA(egl, display)) == null) {
                Log.w("egl-choose", "msaa config not found");
                return chooseDefault(egl, display);
            } else {
                return cof;
            }
        }

        public EGLConfig chooseDefault(EGL10 egl, EGLDisplay display) {
            int attribs[] = {
                    EGL10.EGL_RED_SIZE, 8,
                    EGL10.EGL_GREEN_SIZE, 8,
                    EGL10.EGL_BLUE_SIZE, 8,
                    EGL10.EGL_DEPTH_SIZE, 16,
                    EGL10.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] configCounts = new int[1];
            egl.eglChooseConfig(display, attribs, configs, 1, configCounts);
            if (configCounts[0] == 0) {
                return null;
            } else {
                return configs[0];
            }
        }

        public EGLConfig chooseMSAA(EGL10 egl, EGLDisplay display) {
            int attribs[] = {
                    EGL10.EGL_LEVEL, 0,
                    EGL10.EGL_RENDERABLE_TYPE, 4,
                    EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                    EGL10.EGL_RED_SIZE, 8,
                    EGL10.EGL_GREEN_SIZE, 8,
                    EGL10.EGL_BLUE_SIZE, 8,
                    EGL10.EGL_DEPTH_SIZE, 16,
                    EGL10.EGL_SAMPLE_BUFFERS, 1,
                    EGL10.EGL_SAMPLES, 4,
                    EGL10.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] configCounts = new int[1];
            egl.eglChooseConfig(display, attribs, configs, 1, configCounts);
            if (configCounts[0] == 0) {
                return null;
            } else {
                return configs[0];
            }
        }


    }
}
