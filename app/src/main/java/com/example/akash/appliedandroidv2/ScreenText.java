package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;

public class ScreenText {
    private Bitmap bitmap;
    // get a canvas to paint over the bitmap
    private Canvas canvas;
    private String text;
    private Drawable background;
    private Paint textPaint;

    private int textures[] = new int[9];

    public ScreenText(String s, Context context){
        text = s;
        bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);
        bitmap.eraseColor(0);
        background = context.getDrawable(R.drawable.batman_wall);
        background.setBounds(0, 0, 256, 256);
        background.draw(canvas); // draw the background to our bitmap
        textPaint = new Paint();
        textPaint.setTextSize(32);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
        // draw the text centered
        canvas.drawText(text, 16,112, textPaint);
    }

    public void draw(){
        //Generate one texture pointer...
        GLES20.glGenTextures(1, textures, 0);
//...and bind it to our array
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

//Create Nearest Filtered Texture
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

//Clean up
        bitmap.recycle();
    }
}
