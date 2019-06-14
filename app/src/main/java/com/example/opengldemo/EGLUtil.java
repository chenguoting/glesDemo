package com.example.opengldemo;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.util.Log;

/*
 * Author : chenguoting on 2019-6-12 19:39
 * Email : chen.guoting@nubia.com
 * Company : NUBIA TECHNOLOGY CO., LTD.
 */
public class EGLUtil {
    private final static String TAG = "EGLUtil";

    public static EGLConfig getEGLConfig(EGLDisplay display) throws Exception {
        //int EGL_RECORDABLE_ANDROID = 0x3142;
        EGLConfig[] configs = new EGLConfig[1];
        int[] configSpec = new int[] {
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8 ,
                //EGL14.EGL_DEPTH_SIZE, 16 ,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGLExt.EGL_RECORDABLE_ANDROID, 1, //requires API 26
                EGL14.EGL_NONE };

        int[] numConfig = new int[]{0};
        EGL14.eglChooseConfig(display, configSpec, 0, configs, 0, 1,
                numConfig, 0);
        if(EGL14.eglGetError()==EGL14.EGL_FALSE || numConfig[0]==0){
            throw new Exception("get display config failed");
        }


        return  configs[0];
    }

    public static EGLContext getEGLContext(EGLDisplay display, EGLConfig config) throws Exception {
        EGLContext context;
        int[] attrib_list = { EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE };
        context =  EGL14.eglCreateContext(display, config, EGL14.EGL_NO_CONTEXT, attrib_list, 0);
        int error = EGL14.eglGetError();
        if( error != EGL14.EGL_SUCCESS){
            throw new Exception("fail to get EGLContext error: "+error);
        }
        return context;
    }

    public static EGLDisplay getEGLDisplay() throws Exception{
        EGLDisplay display;
        display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        checkEGLDisplay(display);
        int[] version = new int[2];
        EGL14.eglInitialize(display, version, 0, version, 1);
        return display;
    }

    public static void checkEGLDisplay(EGLDisplay display) throws Exception {
        if(display == EGL14.EGL_NO_DISPLAY || EGL14.eglGetError() != EGL14.EGL_SUCCESS) {
            throw new Exception("no display");
        }else{
            Log.e(TAG,"eglGetDisplay success: "+ display.toString());
        }
    }
}
