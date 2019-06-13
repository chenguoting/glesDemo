package com.example.opengldemo;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.util.Log;

import com.example.opengldemo.shape.Picture;

/*
 * Author : chenguoting on 2019-6-12 19:11
 * Email : chen.guoting@nubia.com
 * Company : NUBIA TECHNOLOGY CO., LTD.
 */
public class MyRenderThread extends Thread {
    private final static String TAG = "MyRenderThread";
    private Object mSurface;
    private int width, height;
    private Context mContext;
    private boolean stop = false;
    private Picture mPicture;

    public MyRenderThread(Object surface, Context context) {
        mSurface = surface;
        mContext = context;
    }


    @Override
    public void run() {
        try {
            EGLDisplay eglDisplay = EGLUtil.getEGLDisplay();
            EGLConfig eglConfig = EGLUtil.getEGLConfig(eglDisplay);
            EGLContext eglContext = EGLUtil.getEGLContext(eglDisplay, eglConfig);

            int[] surfaceAttribs = {
                    EGL14.EGL_NONE
            };
            EGLSurface eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, mSurface,
                    surfaceAttribs, 0);
            EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);

            mPicture = new Picture(mContext.getResources());

            while(!stop) {
                GLES20.glViewport(0, 0, width, height);
                mPicture.draw();
                EGL14.eglSwapBuffers(eglDisplay, eglSurface);
            }

            EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);

            EGL14.eglDestroySurface(eglDisplay, eglSurface);

            EGL14.eglDestroyContext(eglDisplay, eglContext);
        } catch (Exception e) {
            Log.e(TAG, "init env fail", e);
        }
    }

    public void release() {
        stop = true;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
