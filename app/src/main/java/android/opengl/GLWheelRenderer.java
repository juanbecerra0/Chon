package android.opengl;

import android.os.SystemClock;

import com.example.chon.WheelData;
import com.example.chon.WheelDataItem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLWheelRenderer implements GLSurfaceView.Renderer {

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    // Used in rotation animation
    private float angle = 0f;
    private float[] rotationMatrix = new float[16];

    // Wheel data used to create GLWheelShapes
    private WheelData wheelData;
    private GLWheelShape[] wheelShapes;

    public GLWheelRenderer(WheelData wheelData) {
        this.wheelData = wheelData;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set background color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        // For each wheel in wheelData, create a new wheelShape
        wheelShapes = new GLWheelShape[wheelData.getTotalItemCount()];
        Object[] items = wheelData.getWheelDataItemsAsArray();

        int startPoint = 0;
        for (int i = 0; i < wheelShapes.length; i++) {
            wheelShapes[i] = new GLWheelShape( ((WheelDataItem)items[i]), startPoint);
            startPoint += ((WheelDataItem)items[i]).getChance();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Rotation animation
        float[] scratch = new float[16];

        // Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        Matrix.setRotateM(rotationMatrix, 0, angle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        //mSquare.draw(vPMatrix);
        //mSquare.draw(scratch);
        for (GLWheelShape shape : wheelShapes) {
            shape.draw(scratch);
        }
    }

    public void setRotationAngle(float angle) {
        this.angle = angle;
    }
}
