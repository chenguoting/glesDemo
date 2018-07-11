package com.example.opengldemo.shape;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLES32;
import android.opengl.Matrix;
import android.util.Log;

import com.example.opengldemo.GLUtil;
import com.example.opengldemo.R;

import java.nio.FloatBuffer;

/*
 * Author : chenguoting on 2018-7-10 10:24
 * Email : chen.guoting@nubia.com
 * Company : NUBIA TECHNOLOGY CO., LTD.
 *
 * 先用计算着色器将图片变成黑白，再绘制到屏幕
 */
public class BlackAndWhitePicture {
    //计算着色器及参数
    private final int mComputeProgram;
    private int mCTextureHandle;
    private int mCWidthHandle;
    private int mCHeightHandle;
    private int mCImageHandle;

    private final int mProgram;
    private int mPositionHandle;
    private int mTextureCoordHandle;
    private int mMVPMatrixHandle;
    private int mSTMatrixHandle;
    private int mWidthHandle;
    private int mHeightHandle;
    private int mImageHandle;

    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];
    private final int vertexCount = rectCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordBuffer;
    private int mTextureID;
    private int mCTextureID;
    private int width = 144;
    private int height = 144;
    private int local_size_x = 4, local_size_y = 4, local_size_z = 1;//保持和shader中的值一致

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    static final float rectCoords[] = {
            -1, 1, 0,  //左上
            1, 1, 0,  //右上
            -1, -1, 0,  //左下
            1, -1, 0  //右下
    };

    static final float textureCoords[] = {
            0, 0,
            1, 0,
            0, 1,
            1, 1
    };

    public BlackAndWhitePicture(Resources res) {
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.setIdentityM(mSTMatrix, 0);

        vertexBuffer = GLUtil.arrayToBuffer(rectCoords);
        textureCoordBuffer = GLUtil.arrayToBuffer(textureCoords);

        mTextureID = GLUtil.loadTexture(res, R.drawable.ic_launcher);
        mCTextureID = GLUtil.createComputeShaderTexture(GLES31.GL_RGBA32F, width, height);

        int computeShader = GLUtil.loadShader(GLES31.GL_COMPUTE_SHADER,
                GLUtil.readRawFile(res, R.raw.bawp_compute_shader));
        mComputeProgram = GLUtil.createProgram(computeShader);
        mCTextureHandle = GLES20.glGetUniformLocation(mComputeProgram, "uTextureSampler");
        mCWidthHandle = GLES20.glGetUniformLocation(mComputeProgram, "width");
        mCHeightHandle = GLES20.glGetUniformLocation(mComputeProgram, "height");
        mCImageHandle = 0;//不用glGet，但必须和着色器代码中binding的值相同

        int vertexShader = GLUtil.loadShader(GLES20.GL_VERTEX_SHADER,
                GLUtil.readRawFile(res, R.raw.bawp_vertex_shader));
        int fragmentShader = GLUtil.loadShader(GLES20.GL_FRAGMENT_SHADER,
                GLUtil.readRawFile(res, R.raw.bawp_fragment_shader));
        mProgram = GLUtil.createProgram(vertexShader, fragmentShader);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
        mWidthHandle = GLES20.glGetUniformLocation(mProgram, "width");
        mHeightHandle = GLES20.glGetUniformLocation(mProgram, "height");
        mImageHandle = 0;//不用glGet，但必须和着色器代码中binding的值相同

    }


    public void draw() {
        //启用着色器程序
        GLES31.glUseProgram(mComputeProgram);
        //传入数据
        GLES31.glUniform1i(mCWidthHandle, width);
        GLES31.glUniform1i(mCHeightHandle, height);

        GLES31.glActiveTexture(GLES31.GL_TEXTURE3);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mTextureID);
        GLES31.glUniform1i(mCTextureHandle, 3);

        GLES31.glBindImageTexture(mCImageHandle, mCTextureID, 0, false, 0, GLES32.GL_WRITE_ONLY, GLES32.GL_RGBA);
        //分发计算任务
        GLES31.glDispatchCompute(width/local_size_x, height/local_size_y, 1);

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // Enable a handle to the rect vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the rect coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2,
                GLES20.GL_FLOAT, false,
                2*4, textureCoordBuffer);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mSTMatrixHandle, 1, false, mSTMatrix, 0);
        GLES31.glUniform1i(mWidthHandle, width);
        GLES31.glUniform1i(mHeightHandle, height);
        GLES31.glBindImageTexture(mImageHandle, mCTextureID, 0, false, 0, GLES32.GL_READ_ONLY, GLES32.GL_RGBA32F);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
    }
}
