package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Akash on 12/26/2017.
 */

public class GameLoop_Class extends SurfaceView implements Runnable{
    SurfaceHolder surfaceHolder;
    boolean canDraw = false;
    Canvas canvas;
    Thread thread = null;
    GameLoop glHelperVar;
   int[][] walls ={{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
                   {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                   {1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
                   {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                   {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1},
                   {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                   {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
                   {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
           {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
           {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

 /*   int walls[][] = {{1,1,1,1,1,1,1,1},
                     {1,0,0,1,1,0,0,1}};*/

    int scrWid = this.getScreenWidth();
    int scrHi = this.getScreenHeight();

    double frames_per_second, frameTimeS, frameTimeMS, frameTimeNS, physicsRate, dt, dtPool;
    double lastFrame, endOfRender, deltaT;

    ArrayList<Integer> touchX;
    ArrayList<Integer> touchY;


    Bitmap backGround;
    Bitmap redGem;
    Bitmap ball;
    int speedX = 10;
    int speedY = 10;
    int bx,by;
    Path square;
    Paint redSt, greenSt, blueSt, blackSt, skBlueSt;
    Paint redFill, blueFill, greenFill, whiteFill, greyFill, ltPurpleFill;
    Alien alien;
    Rect alienR;
    boolean isInitialized = false;

    private boolean menuOpened = false;

    String getvalue;
    InputMethodManager imm;
    StringBuilder stringBuilder;
    //TextView value;
    String ch = "";
    Context super_CX;


    public GameLoop_Class(Context context) {
        super(context);
        glHelperVar = new GameLoop();

        super_CX = context;
        surfaceHolder = getHolder();
        canDraw = false;

        backGround = BitmapFactory.decodeResource(getResources(),R.drawable.batman_wall);
        ball = BitmapFactory.decodeResource(getResources(),R.drawable.wall);
        ball = Bitmap.createScaledBitmap(ball,scrWid/(walls[0].length),scrWid/(walls[0].length),false);
        //Log.d("Lenth should be:",Integer.toString(scrWid/walls.length));
        //redGem = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        //redGem = Bitmap.createScaledBitmap(redGem, 120, 120, false);
        //redGem.recycle();
        alien = new Alien(110,110,scrWid,scrHi, 1400,1400);
        alienR = alien.getAlien();

        touchX = new ArrayList<Integer>();
        touchY = new ArrayList<Integer>();

        bx=0;
        by=0;

        frames_per_second = 80;
        frameTimeS = 1/frames_per_second;
        frameTimeMS = frameTimeS*1000;
        frameTimeNS = frameTimeMS*1000000;
        dtPool =0;

        physicsRate = 50;
        dt = (1/physicsRate)*1000000000;
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getContext().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
*/
        //back = Bitmap.createScaledBitmap(backGround, getWidth(), getHeight(), false);
        //
        //backGround.setHeight(100);
        //backGround.setWidth(80);
    }

    @Override
    public void run() {

        lastFrame = System.nanoTime();
        deltaT = 0;
        paints();

        while(canDraw){

            if(!surfaceHolder.getSurface().isValid())
            {
                continue;
            }
            updateBigDelta(deltaT);
            //if(!isInitialized){
              //  canvas.drawRect(500,1200,1000,1600,blueFill);
                //canvas.drawText("Select a point",750,1400,redFill);
                //initializeAlien(touchX.get(0), touchY.get(0));
           // }else {
               draw();
            //}
            //draw2(ch);

            endOfRender = System.nanoTime();
            deltaT = frameTimeNS - (lastFrame - endOfRender);
            //stats();
            try {
                if(deltaT>0)
                    thread.sleep((long)(deltaT/1000000));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lastFrame = System.nanoTime();
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

    public void draw(){

        canvas = surfaceHolder.lockCanvas();
        canvas.drawBitmap(backGround,0,0,null);

        int lenX = scrWid/(walls[0].length);
        int lenH = lenX;

        int cx = 0;
        int cy = 0;

        Log.d("Initial cx: ",Integer.toString(cx));
        Log.d("Initial cy: ",Integer.toString(cy));
        for(int i=0;i<walls.length;i++){
            for(int j=0;j<walls[i].length;j++) {
                if (walls[i][j] != 0) {
                    //TargetRectangle tr = new TargetRectangle(cx, cy, lenX, lenH);
                    //tr.drawIt(canvas);
                    canvas.drawBitmap(ball,cx,cy,null);
                }
                Log.d("Looped cx: ",Integer.toString(cx));
                cx += lenX;
            }
            //cx = lenX/2;
            cx=0;
            Log.d("Looped cy: ",Integer.toString(cy));
            cy+=lenH;
        }

        for(int i=0;i<touchX.size();i++) {
            canvas.drawCircle(touchX.get(i), touchY.get(i), 50, blueFill);
        }
        alien.updateNextLocation(walls);
        alien.updateLocation(walls);
        canvas.drawRect(alienR,blueFill);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void updateBigDelta(double time){
        if(time<0){
            time = frameTimeNS - time;
        }
        time+=dtPool;
        dtPool = 0;

        while(time>=dt){
            update(dt);
            time = time-dt;
        }

        dtPool+=time;
    }

    public void update(double time){
        int t = (int)(time/1000000);
        Log.d("t is :", Integer.toString(t));
        ballAnimation(1,t );
    }

    public void initializeAlien(int tx, int ty){
        alien = new Alien(110,110,scrWid,scrHi, tx,ty);
        alienR = alien.getAlien();
        isInitialized = true;
    }

    public void ballAnimation(int speed, int time){
        if(bx>=scrWid-90){
            speedX = -speed;
        }else if(bx<=0) speedX = speed;
        if(by>=scrHi-180){
            speedY = -speed;
        }else if(by<=0) speedY = speed;

        bx+=(speedX*time);
        by+=(speedY*time);
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

        whiteFill = new Paint();
        whiteFill.setColor(Color.WHITE);
        whiteFill.setStyle(Paint.Style.FILL);

        greyFill = new Paint();
        greyFill.setColor(Color.rgb(56,56,58));
        greyFill.setStyle(Paint.Style.FILL);

        ltPurpleFill = new Paint();
        ltPurpleFill.setColor(Color.rgb(160,118,188));
        ltPurpleFill.setStyle(Paint.Style.FILL);

        redSt = new Paint();
        redSt.setColor(Color.RED);
        redSt.setStyle(Paint.Style.STROKE);
        redSt.setStrokeWidth(10);

        blackSt = new Paint();
        blackSt.setColor(Color.BLACK);
        blackSt.setStyle(Paint.Style.STROKE);
        blackSt.setStrokeWidth(5);

        skBlueSt = new Paint();
        skBlueSt.setColor(Color.rgb(107, 170, 186));
        skBlueSt.setStyle(Paint.Style.STROKE);
        skBlueSt.setStrokeWidth(15);

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



    public void stats(){
        Log.d("frames per second", Double.toString(frames_per_second));
        Log.d("deltaT", Double.toString(deltaT));
        Log.d("frameTimeS", Double.toString(frameTimeS));
        Log.d("frameTimeMS", Double.toString(frameTimeMS));
        Log.d("frameTimeNS", Double.toString(frameTimeNS));
        Log.d("(bx) =", Integer.toString(bx));
        Log.d("(by) =", Integer.toString(by));
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int e = event.getAction();

        if(e==event.ACTION_DOWN){
            //canvas.drawCircle(500,500,50,blueSt);
            touchX.add((int)event.getRawX());
            touchY.add((int)event.getRawY());
            Toast myToast = Toast.makeText(getContext(),"Pressed at: "+(int)event.getRawX()+" : "+(int)event.getRawY(), Toast.LENGTH_SHORT);
            myToast.show();
            if((int)event.getRawX()> 550 && (int)event.getRawX() < 850 && (int)event.getRawY() < 1350){
                imm = (InputMethodManager) super_CX.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, 0);
                //stringBuilder = new StringBuilder();
            }
            if((int)event.getRawX()> 50 && (int)event.getRawX() < 180 && (int)event.getRawY() < 200){
                menuOpened = true;
            }else menuOpened = false;
        }

        return super.onTouchEvent(event);
    }


    public void setInput(String s){
        this.ch = s;
    }

    public void drawText(String c){
        //canvas = surfaceHolder.lockCanvas();
        blueFill.setTextSize(40f);
        canvas.drawText(c,700,1300,blueFill);
        //surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void draw2(String c){
        canvas = surfaceHolder.lockCanvas();
        TargetRectangle back = new TargetRectangle(this.scrWid/2,this.scrHi/2,this.scrWid,this.scrHi, whiteFill);
        back.drawIt(canvas);
        drawToolbarStuff();

        drawInstructions("> Instructions on how the App works:\n" +
                        "> Enter a number in the box below to choose the difficulty\n" +
                "> of the chess bot (Either 1 or 2)\n" +
                ">> Go on.. Give it a Try!");

        //textbox
        TargetRectangle r = new TargetRectangle(700,1300,300,100, redSt);
        r.drawIt(canvas);
        drawText(c);

        drawMenu();
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawToolbarStuff(){
        TargetRectangle tbarBack = new TargetRectangle(this.scrWid/2, 100, this.scrWid, 200, greyFill);
        tbarBack.drawIt(canvas);

        TargetRectangle menu1 = new TargetRectangle(150, 100, 125, 25, whiteFill);
        menu1.drawIt(canvas);
        menu1 = new TargetRectangle(120, 60, 125, 25, whiteFill);
        menu1.drawIt(canvas);
        menu1 = new TargetRectangle(120, 140, 125, 25, whiteFill);
        menu1.drawIt(canvas);
    }

    private void drawMenu(){
        if(menuOpened){
            TargetRectangle menuBack = new TargetRectangle(0,200,(this.scrWid/2)/2,800,redSt, whiteFill);
            menuBack.drawIt(canvas);
        }
    }

    private void drawInstructions(String inst){
        int x = 130;
        int y = 400;
        TargetRectangle instBack = new TargetRectangle(100, 300, 1300, 800, blackSt, ltPurpleFill);
        instBack.drawIt(canvas);
        blackSt.setTextSize(60f);
        for(int i=0;i<inst.length();i++) {
            if(inst.charAt(i) != '\n') {
                canvas.drawText(inst.substring(i, i + 1), x, y, blackSt);
                x+=30;
            }else{
                x = 130;
                y+=70;
            }
        }
    }
}
