package com.example.opengldemo;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
 * Author : chenguoting on 2019-6-13 8:48
 * Email : chen.guoting@nubia.com
 * Company : NUBIA TECHNOLOGY CO., LTD.
 */
public class MySurfaceView extends SurfaceView {
    private final static String TAG = "MySurfaceView";

    public MySurfaceView(final Context context) {
        super(context);

        getHolder().addCallback(new SurfaceHolder.Callback() {
            MyRenderThread renderThread;
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "surfaceCreated");
                renderThread = new MyRenderThread(holder, context);
                renderThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged");
                renderThread.setSize(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "surfaceDestroyed");
                renderThread.release();
            }
        });

    }
}
