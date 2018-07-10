package com.example.opengldemo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.opengldemo.shape.Rect;
import com.example.opengldemo.shape.Square;
import com.example.opengldemo.shape.Triangle;
import com.example.opengldemo.shape.Picture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private Square   mSquare;
    private Rect mRect;
    private Picture mPicture;
    private Context mContext;

    public MyGLRenderer(Context c) {
        mContext = c;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        // initialize a triangle
        mTriangle = new Triangle();
        // initialize a square
        mSquare = new Square();
        // initialize a rect
        mRect = new Rect(mContext.getResources());

        mPicture = new Picture(mContext.getResources());
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //mRect.draw();
        mPicture.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
