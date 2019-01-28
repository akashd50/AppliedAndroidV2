package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.sql.Time;

/**
 * Created by Akash on 12/11/2017.
 */

public class SecondActivity_ViewClass extends View {
    Bitmap aBM;
    float bx = 0;
    float by = 0;
    float xDir = 1;
    float yDir = 1;
    Paint redFill, blueFill, greenFill;
    Paint redStroke, blueStroke, greenStroke;
    Canvas canvas;
    public SecondActivity_ViewClass(Context context) {
        super(context);
        setBackgroundResource(R.drawable.batman_wall);
        canvas = new Canvas();
    }

    @Override
    protected void onDraw(Canvas canvass) {
        super.onDraw(canvass);
        canvas = canvass;
        //Color darkBlue = new Color();
        //darkBlue.rgb(78, 117, 130);
        redFill = new Paint();
        redFill.setColor(Color.rgb(78, 117, 130));
        redFill.setStyle(Paint.Style.FILL);
        redFill.setTextSize(100);
        aBM = BitmapFactory.decodeResource(getResources(), R.drawable.abm2);

        bx = bx+xDir;
        by= by+yDir;
        //canvas.drawText("New Game", 100, 100, redFill);
        //drawText("New Game", x,y, redFill);
        if(bx>canvass.getWidth()){
            xDir = -5;
        }else if(bx<=0) xDir = 5;
        if(by>canvass.getHeight()){
            yDir = -5;
        }else if(by<=0) yDir = 5;

        canvass.drawBitmap(aBM, bx, by, null);

        invalidate();
    }

    public void drawText(String s, float x, float y, Paint p){
        canvas.drawText(s, x, y, p);
       // Time t = new Time(0);
        //t.setTime(0);
       // int xx = 0;
        //int yy = 0;
        //aBM = BitmapFactory.decodeResource(getResources(), R.drawable.abm2);
        //while(xx<1000 && yy<1000) {
            //canvas.drawBitmap(aBM, x, y, null);
          //  xx++;
           // yy++;
            /*try {
                wait(1000);
            }catch(Exception e){}*/
        }
    }
