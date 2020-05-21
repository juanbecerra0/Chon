package com.example.chon;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private OpenGLSquare square;
    public OpenGLRenderer(Context c){
        context = c;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //square = new OpenGLSquare(context);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.9f,0.9f,0.9f,1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //square.draw();
    }
}
