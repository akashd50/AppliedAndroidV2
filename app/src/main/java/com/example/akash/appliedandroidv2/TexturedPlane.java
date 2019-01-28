package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;

public class TexturedPlane {
    // in this case 1.
    private int[] textures = new int[1];
    private int mTextureId;
    private Bitmap bitmap;
    private FloatBuffer mTextureBuffer;
// Tell OpenGL to generate textures.
    private Context context;
    private float cx,cy,cz,l,h;
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private int colorCount;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private final int COLOR_COMPONENTS_COUNT = 4;
    private static final int COORDS_PER_VERTEX = 3;
    private int vertexCount;
    private int vertexStride = (COORDS_PER_VERTEX )* 4; // 4 bytes per vertex

    private int mMVPMatrixHandle, aTextureHandle, textureUniform;

    private float transformY,transformX,transformZ;

    private final String vertexShader =
            "uniform mat4 u_Matrix;" +
            "attribute vec4 a_Position;" +
            "attribute vec2 a_TextureCoordinates;" +
            "varying vec2 v_TextureCoordinates;" +
            "void main()" +
            "{" +
            "v_TextureCoordinates = a_TextureCoordinates;" +
            "gl_Position = u_Matrix * a_Position;" +
            "}";

    private final String fragmentShader =
                "precision mediump float;" +
                "uniform sampler2D u_TextureUnit;" +
                "varying vec2 v_TextureCoordinates;" +
                "void main()" +
                "{" +
                "gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);" +
                "}";

    public TexturedPlane(float cx,float cy, float cz, float l, float h, Context ctx, int resID, Bitmap bm){
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.l = l;
        this.h = h;
        transformX=0f;
        transformY=0f;
        transformZ=0f;

        float v1[] = {cx,cy,cz,
                cx-(l/2),cy-(h/2),cz,
                cx+(l/2),cy-(h/2),cz,
                cx+(l/2),cy+(h/2),cz,
                cx-(l/2),cy+(h/2),cz,
                cx-(l/2),cy-(h/2),cz};

        float[] textureCoords = {0.5f, 0.5f,
                                 0f, 1.0f,
                                 1f, 1.0f,
                                 1f, 0.0f,
                                 0f, 0.f,
                                 0f, 1.0f };

        vertexCount = v1.length/COORDS_PER_VERTEX ;
        ByteBuffer bb = ByteBuffer.allocateDirect(v1.length * 4);
        if(bb!=null) {
            // (number of coordinate values * 4 bytes per float)

            // use the device hardware's native byte order
            bb.order(ByteOrder.nativeOrder());

            // create a floating point buffer from the ByteBuffer
            vertexBuffer = bb.asFloatBuffer();
            // add the coordinates to the FloatBuffer
            vertexBuffer.put(v1);
            // set the buffer to read the first coordinate
            vertexBuffer.position(0);
            // float is 4 bytes, therefore we multiply the number if
            // vertices with 4.
            ByteBuffer byteBuf = ByteBuffer.allocateDirect(
                    textureCoords.length * 4);
            byteBuf.order(ByteOrder.nativeOrder());
            mTextureBuffer = byteBuf.asFloatBuffer();
            mTextureBuffer.put(textureCoords);
            mTextureBuffer.position(0);
            generateProgram();
            loadTexture(ctx,resID, bm);
        }

    }

    private int loadTexture(Context context, int resID, Bitmap bm){
        textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        if(bm==null) {
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(), resID, options);
        }else{
            bitmap=bm;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textures[0];
    }

    private void drawInternal(float[] mMVPMatrix){
        GLES20.glUseProgram(mProgram);
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Enable a handle to the triangle vertices
        // get handle to vertex shader's vPosition member
        textureUniform = GLES20.glGetUniformLocation(mProgram,"u_TextureUnit");
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        GLES20.glUniform1i(textureUniform, 0);
        // Prepare the triangle coordinate datav
        //vertexBuffer.position(0);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        mTextureBuffer.position(0);
        aTextureHandle = GLES20.glGetAttribLocation(mProgram,"a_TextureCoordinates");
        GLES20.glVertexAttribPointer(aTextureHandle,2,GLES20.GL_FLOAT,false,8,mTextureBuffer);
        GLES20.glEnableVertexAttribArray(aTextureHandle);

        // get handle to fragment shader's vColor member
        /*mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        //vertexBuffer.position(COORDS_PER_VERTEX); //start from 3... reading color data from the matrix
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle,COLOR_COMPONENTS_COUNT,
                GLES20.GL_FLOAT,false,
                COLOR_COMPONENTS_COUNT*4,colorBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);*/
        // Set color for drawing the triangle
        // GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // Draw the triangle
       GLES20.glEnable( GL_BLEND );
       GLES20.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
        //GLES20.glDrawArrays(GLES20.GL_LINES);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void draw(float[] mMVPMatrix){
        float[] scratcht = new float[16];
        float[] tempMoveMat = new float[16];
        Matrix.setIdentityM(tempMoveMat, 0);
        Matrix.translateM(tempMoveMat, 0, transformX, transformY, transformZ);
        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPMatrix, 0);
        this.drawInternal(scratcht);
    }

    private void generateProgram() {
        int vertexShad = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShader);
        int fragmentShad = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShader);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShad);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShad);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void updateTransformY(float vy){ transformY+=vy; }
    public float getTransformY(){return this.transformY;}

    public void updateTransformX(float vx){ transformX+=vx; }
    public void changeTransformX(float vx){ transformX =vx; }
    public void changeTransformY(float vx){ transformY =vx; }
    public float getTransformX(){return this.transformX;}

    public void translate(float x,float y,float z){
        this.transformX = x;
        this.transformY = y;
        this.transformZ = z;
    }
}
