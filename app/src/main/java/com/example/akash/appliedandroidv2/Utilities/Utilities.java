package com.example.akash.appliedandroidv2.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.akash.appliedandroidv2.R;
import com.example.akash.appliedandroidv2.TexturedPlane;

public class Utilities {
    private static Context context;
    public static TexturedPlane[] CHARS_ARRAY = new TexturedPlane[127];
    public static boolean TEXTURES_LOADED = false;

    public Utilities(Context c){
        context = c;
    }

    public static float getScreenHeightPixels(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidthPixels(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static float getStatusBarHeightPixels() {
        float result = 0;
        float resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize((int)resourceId);
        }
        return result;
    }
    public static float getScreenTop(){
        return (getScreenHeightPixels() - getStatusBarHeightPixels())/getScreenWidthPixels();
    }

    public static float getScreenBottom(){
        return -(getScreenHeightPixels() - getStatusBarHeightPixels())/getScreenWidthPixels();
    }

    public static TexturedPlane getText(String text, boolean single){
        Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_4444);
        /*if(single) {
            bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_4444);
        }else{
            bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
        }*/
        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0);

        // get a background image from resources
        // note the image format must match the bitmap format
        /*Drawable background = context.getDrawable(R.drawable.pattern_wall_1);
        background.setBounds(0, 0, 256, 256);
        background.draw(canvas); // draw the background to our bitmap*/

        // Draw the text
        Paint textPaint = new Paint();
        textPaint.setTextSize(64);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xff, 0xa1, 0x00, 0x00);
        // draw the text centered
        canvas.drawText(text, 16,48, textPaint);

        TexturedPlane tp = new TexturedPlane(0.0f,0f,-3.0f,0.1f,0.1f,context, R.drawable.pattern_wall_1,bitmap);
        // bitmap.recycle();
        return tp;
    }

    public static void initialzeTextBms(){
        for(int i=0;i<127;i++){
            String s = (char)(i)+"";
            CHARS_ARRAY[i] = getText(s, true);
        }
        TEXTURES_LOADED = true;
    }
}
