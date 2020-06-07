package android.opengl;

import com.example.chon.WheelData;
import com.example.chon.WheelDataItem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

public class GLWheelShape {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    /*
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    */
    private float wheelCoords[];
    private short drawOrder[];
    private float[] color;

    //------------------------
    // Shader program
    //------------------------

    private final int mProgram;
    private int vPMatrixHandle;

    //------------------------
    // Drawing
    //------------------------

    private int positionHandle;
    private int colorHandle;

    private int vertexCount;
    private int vertexStride;

    public GLWheelShape(WheelDataItem item, int startPoint) {
        // Init coordinate vars
        wheelCoords = new float[(2 + item.getChance()) * 3];
        drawOrder = new short[3 * item.getChance()];

        // Init vertex count and stride
        vertexCount = wheelCoords.length / COORDS_PER_VERTEX;
        vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

        // Calculate init points
        // Center
        wheelCoords[0] = 0f;
        wheelCoords[1] = 0f;
        wheelCoords[2] = 0f;
        // First point
        wheelCoords[3] = (float) Math.sin(((double)startPoint / 100) * 2 * Math.PI);
        wheelCoords[4] = (float) Math.cos(((double)startPoint / 100) * 2 * Math.PI);
        wheelCoords[5] = 0f;

        // Keep track of indexes
        int coordIndex = 6;
        int drawIndex = 0;

        // Calculate wheel coordinates and their draw order
        for (int i = 1; i <= item.getChance(); i++) {
            // Calculate the lerp amount
            double lerpAmount = ((double)startPoint + i) / 100;

            // Calculate a new point
            wheelCoords[coordIndex++] = (float) Math.sin(lerpAmount * 2 * Math.PI);
            wheelCoords[coordIndex++] = (float) Math.cos(lerpAmount * 2 * Math.PI);
            wheelCoords[coordIndex++] = 0f;

            // Index this new point with existing points
            drawOrder[drawIndex++] = 0; // Center
            drawOrder[drawIndex++] = (short)(i + 1); // Point created
            drawOrder[drawIndex++] = (short)(i); // Last point created
        }

        // Determine a random color for this portion
        color = item.getColor();

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                wheelCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(wheelCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // Compile shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the square vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the square coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the square
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the square
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
