package android.opengl;

import android.content.Context;
import android.util.AttributeSet;

import com.example.chon.WheelData;

public class GLWheelView extends GLSurfaceView {

    private GLWheelRenderer renderer;

    public GLWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
    }

    public void initRenderer(WheelData wd) {
        renderer = new GLWheelRenderer(wd);

        // Set the renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}
