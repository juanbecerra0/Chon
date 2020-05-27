package android.opengl;

import android.content.Context;
import android.util.AttributeSet;

public class GLWheelView extends GLSurfaceView {

    private final GLWheelRenderer renderer;

    public GLWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new GLWheelRenderer();

        // Set the renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}
