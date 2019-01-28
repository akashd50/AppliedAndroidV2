package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Akash on 12/17/2017.
 */

public class CanvasDrawClass extends SurfaceView implements Runnable {
    Canvas canvas;
    SurfaceHolder surfaceHolder;
    Thread thread =null;
    boolean canDraw = false;
    Bitmap backGround;
    Bitmap ball;
    int bx,by;
    Path square;
    Paint redSt, greenSt, blueSt;
    Paint redFill, blueFill, greenFill;
    //Bitmap back;
    public CanvasDrawClass(Context context) {
        super(context);
        surfaceHolder = getHolder();
        backGround = BitmapFactory.decodeResource(getResources(),R.drawable.batman_wall);
        ball = BitmapFactory.decodeResource(getResources(),R.drawable.abm2);
        bx=0;
        by=0;
        //back = Bitmap.createScaledBitmap(backGround, getWidth(), getHeight(), false);
        //
        //backGround.setHeight(100);
        //backGround.setWidth(80);
    }

    @Override
    public void run() {
        paints();
        while(canDraw){
            if(!surfaceHolder.getSurface().isValid())
            {
                continue;
            }
            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(backGround,0,0,null);
            square(100,100,500,500);
            ballAnimation(1);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        canDraw = false;
        while(true){
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    public void resume(){
        canDraw = true;
        thread = new Thread(this);
        thread.start();
    }

    public void paints(){
        redFill = new Paint();
        redFill.setColor(Color.RED);
        redFill.setStyle(Paint.Style.FILL);

        greenFill = new Paint();
        greenFill.setColor(Color.GREEN);
        greenFill.setStyle(Paint.Style.FILL);

        blueFill = new Paint();
        blueFill.setColor(Color.BLUE);
        blueFill.setStyle(Paint.Style.FILL);

        redSt = new Paint();
        redSt.setColor(Color.RED);
        redSt.setStyle(Paint.Style.STROKE);
        redSt.setStrokeWidth(10);

        blueSt = new Paint();
        blueSt.setColor(Color.BLUE);
        blueSt.setStyle(Paint.Style.STROKE);
        blueSt.setStrokeWidth(10);

        greenSt = new Paint();
        greenSt.setColor(Color.GREEN);
        greenSt.setStyle(Paint.Style.STROKE);
        greenSt.setStrokeWidth(10);
    }

    public void square(int x1, int y1, int x2, int y2){
        square = new Path();
        square.moveTo(x1,y1);
        square.lineTo(x2,y1);
        square.moveTo(x2,y1);
        square.lineTo(x2,y2);
        square.moveTo(x2,y2);
        square.lineTo(x1,y2);
        square.moveTo(x1,y2);
        square.lineTo(x1,y1);

        canvas.drawPath(square,redSt);
    }

    public void ballAnimation(int speed){

        /*if(bx == 100 && by < 500){
            by+=speed;
        }
        if(by == 500 && bx < 500){
            bx+=speed;
        }
        if(bx == 500 && by > 100){
            by -=speed;
        }
        if(by==500 && bx >100){
            bx-=speed;
        }*/
        canvas.drawBitmap(ball,bx,by,null);
        bx+=speed;
        by+=speed;


    }
}
