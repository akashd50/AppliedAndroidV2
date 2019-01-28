package com.example.akash.appliedandroidv2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Akash on 1/15/2018.
 */

public class Box extends Objects {

    Bitmap box;
    private int locX, locY;

    public Box(){
        super();
    }

    public Box(int x, int y){
        super(x,y);
        locX = x;
        locY = y;

        //box = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.red);
        //box.createScaledBitmap()
    }

    public void drawIt(Canvas c){
       // c.drawRect();
    }
}
