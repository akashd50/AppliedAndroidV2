package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.akash.appliedandroidv2.ChessGameActivity;
import com.example.akash.appliedandroidv2.TargetRectangle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.akash.appliedandroidv2.chessFiles.*;

public class ChessGameClass extends SurfaceView implements Runnable {

    SurfaceHolder surfaceHolder;
    boolean canDraw = false;
    Canvas canvas;
    Thread thread = null;
    ChessGameActivity cgHelperVar;
    ChessBoard chessBoard;
    TargetRectangle textBox,instBack,background,activityLaunch,tbarBack, menuBack, menuIc1,
            outputFeedbackBox,menuIcBack;
    private String outputText = "";
    private int outputLineCount = 0;

    private EasyAI easyAI;
    private boolean botsTurn;

    int scrWid = this.getScreenWidth();
    int scrHi = this.getScreenHeight();

    double frames_per_second, frameTimeS, frameTimeMS, frameTimeNS, physicsRate, dt, dtPool;
    double lastFrame, endOfRender, deltaT;

    ArrayList<Integer> touchX;
    ArrayList<Integer> touchY;



    private int sX, sY, sLoc;

    Paint redSt, greenSt, blueSt, blackSt, skBlueSt, darkGreySt, whiteSt;
    Paint redFill, blueFill, greenFill, whiteFill, greyFill, ltPurpleFill, mattBlackFill, shadowColor;

    Pieces playerCurrPiece;
    LinkedList currPieceMoveList;

    private boolean menuOpened = false;
    private boolean altBack = false;

    Context super_CX;

    private int instClicks = 0;

    private int stage = 0;
    //stages:
    // 0 = initialization
    // 1 = instructons
    // 2 =


    public ChessGameClass(Context context) {
        super(context);
        cgHelperVar = new ChessGameActivity();

        super_CX = context;
        surfaceHolder = this.getHolder();
        canDraw = false;
        sX = 130;
        sY = 400;
        sLoc = 0;
        touchX = new ArrayList<Integer>();
        touchY = new ArrayList<Integer>();
        botsTurn = false;

        /*TargetRectangle textBox = new TargetRectangle();
        TargetRectangle instBack = new TargetRectangle();
        TargetRectangle background= new TargetRectangle();
        TargetRectangle activityLaunch= new TargetRectangle();
        TargetRectangle tbarBack= new TargetRectangle();
        TargetRectangle menuBack= new TargetRectangle();
        TargetRectangle menuIc1= new TargetRectangle();
        outputFeedbackBox = new TargetRectangle();*/

        frames_per_second = 80;
        frameTimeS = 1 / frames_per_second;
        frameTimeMS = frameTimeS * 1000;
        frameTimeNS = frameTimeMS * 1000000;
        dtPool = 0;

        physicsRate = 50;
        dt = (1 / physicsRate) * 1000000000;
        paints();
        chessBoard = new ChessBoard(scrWid,ltPurpleFill,skBlueSt, mattBlackFill);
        chessBoard.initializeBoard();
        easyAI = new EasyAI("Jarvis");
        easyAI.initializeAI(chessBoard);

    }

    @Override
    public void run () {

        lastFrame = System.nanoTime();
        deltaT = 0;
        //paints();

        while (canDraw) {

            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            updateBigDelta(deltaT);

            draw2("");

            endOfRender = System.nanoTime();
            deltaT = frameTimeNS - (lastFrame - endOfRender);
            //stats();
            try {
                if (deltaT > 0)
                    thread.sleep((long) (deltaT / 1000000));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lastFrame = System.nanoTime();
        }
    }

    private void drawBackground(){
        if(!altBack) {
            background = new TargetRectangle(this.scrWid / 2, this.scrHi / 2, this.scrWid, this.scrHi, whiteFill);
            background.drawIt(canvas);
        }else{
            background  = new TargetRectangle(this.scrWid / 2, this.scrHi / 2, this.scrWid, this.scrHi, mattBlackFill);
            background.drawIt(canvas);
        }

    }

    public void draw2 (String c){
        canvas = surfaceHolder.lockCanvas();
       drawBackground();
        drawToolbarStuff();

        chessBoard.drawBoard(canvas, playerCurrPiece, currPieceMoveList,tbarBack.getHeight());

        drawOutputBox();
        showOutput(outputFeedbackBox,outputText);
        drawMenu();
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    /*private void drawToolbarStuff () {
        tbarBack = new TargetRectangle(this.scrWid / 2, 100, this.scrWid, 200, greyFill);
        tbarBack.drawIt(canvas);

        TargetRectangle menu1 = new TargetRectangle(120, 100, 125, 25, whiteFill);
        menu1.drawIt(canvas);
        menu1 = new TargetRectangle(120, 60, 125, 25, whiteFill);
        menu1.drawIt(canvas);
        menu1 = new TargetRectangle(120, 140, 125, 25, whiteFill);
        menu1.drawIt(canvas);
    }*/

    private void drawOutputBox(){
        blackSt.setStrokeWidth(20);
        TargetRectangle background = new TargetRectangle(scrWid/2,
                scrWid+tbarBack.getHeight() + (scrHi-scrWid+tbarBack.getHeight())/2,scrWid,
                scrHi-scrWid+tbarBack.getHeight(),ltPurpleFill);

        background.drawIt(canvas);
        int cx = scrWid*2/100;
        int cy = scrWid+tbarBack.getHeight()+scrWid*2/100;
        outputFeedbackBox = new TargetRectangle(cx,cy,scrWid- 2*(cx),scrHi- (cy + 100), whiteSt,blackSt, mattBlackFill);

        outputFeedbackBox.drawIt(canvas);
    }

    private void addTextToOutput(String s){
        outputText+= s+"\n";
        outputLineCount++;
        if(outputLineCount>15){
            for(int i=0;i<outputText.length();i++){
                if(outputText.charAt(i)=='\n'){
                    outputText = outputText.substring(i+1);
                    break;
                }
            }
        }
    }

    private void showOutput(TargetRectangle tb,String s){
        //canvas = surfaceHolder.lockCanvas();
        whiteSt.setTextSize(40f);
        whiteSt.setStrokeWidth(3);
        int iy = tb.getTop()+20;
        if(s.compareTo("")!=0) {
            String temp = "";
            for(int i=0;i<s.length();i++){
                if(s.charAt(i)!='\n'){
                    temp+=s.charAt(i);
                }else{
                    iy +=50;
                    canvas.drawText(temp, tb.getLeft() + 20, iy, whiteSt);
                    temp = "";
                }
            }

        }
       // surfaceHolder.unlockCanvasAndPost(canvas);
    }

    /*private void drawMenu () {
        if (menuOpened) {
            menuBack = new TargetRectangle(0, 200, (this.scrWid / 2) / 2, 800, redSt, whiteFill);
            menuBack.drawIt(canvas);
            menuIc1 = new TargetRectangle(1,201,(this.scrWid / 2) / 2, 800/4,redSt,whiteFill);
            menuIc1.drawIt(canvas);
            TargetRectangle icon;
            if(!altBack) {
                icon = new TargetRectangle(menuIc1.getLeft() + 50, menuIc1.getTop() + 60, 100, 100, redSt,mattBlackFill);
            }else{
                icon = new TargetRectangle(menuIc1.getLeft() + 50, menuIc1.getTop() + 60, 100, 100, redSt,whiteFill);
            }
            icon.drawIt(canvas);
            blackSt.setStrokeWidth(5);
            canvas.drawText("Swap", menuIc1.getLeft()+menuIc1.getLength()/2, menuIc1.getTop()+60,blackSt);
            blackSt.setStrokeWidth(10);
        }
    }*/
    private void drawToolbarStuff () {
        int backH = scrHi*6/100;
        tbarBack = new TargetRectangle(this.scrWid / 2, backH/2/*scrHi * 3/100*/, this.scrWid, backH, greyFill);
        tbarBack.drawIt(canvas);
        int menuH = backH*10/100;
        int diff = backH*15/100;
        menuIcBack = new TargetRectangle(this.scrWid / 10 - 125/2, scrHi * 3/100 - (menuH*3+diff*2)/2, 125, menuH*3+diff*2, whiteSt, blueFill);
        //menuIcBack.drawIt(canvas);
        TargetRectangle menu1 = new TargetRectangle(this.scrWid / 10, scrHi * 3/100, 125, menuH, whiteFill);
        menu1.drawIt(canvas);
        menu1 = new TargetRectangle(this.scrWid / 10, scrHi * 3/100 -diff , 125, menuH, whiteFill);
        menu1.drawIt(canvas);
        menu1 = new TargetRectangle(this.scrWid / 10, scrHi * 3/100 + diff, 125, menuH, whiteFill);
        menu1.drawIt(canvas);
    }

    private void drawMenu () {
        if (menuOpened) {
            TargetRectangle shadwBack = new TargetRectangle(this.scrWid/2, this.scrHi/2, this.scrWid, this.scrHi,shadowColor);
            shadwBack.drawIt(canvas);

            //Menu BAckground
            menuBack = new TargetRectangle(0, 0, (this.scrWid / 2), this.scrHi, blackSt, whiteFill);
            menuBack.drawRightStroked(canvas);

            //MenuTitle Text
            mattBlackFill.setStrokeWidth(3);
            mattBlackFill.setTextSize((this.scrWid / 2)*5/100 + tbarBack.getHeight() * 40/100);
           // blackSt.setStrokeWidth(5);
            TargetRectangle title = new TargetRectangle(0, 0, (this.scrWid / 2), tbarBack.getHeight(), blackSt, whiteFill);
            title.drawBottomStroked(canvas);
            canvas.drawText("Options", title.getLeft()+title.getLength()*5/100, title.getTop()+ title.getHeight()*80/100,mattBlackFill);

            //First Menu Icon (Reset the board)
            blackSt.setStrokeWidth(2);
            menuIc1 = new TargetRectangle(0,tbarBack.getHeight(),this.scrWid / 2, scrHi/10,blackSt,whiteFill);
            menuIc1.drawBottomStroked(canvas);

            /*TargetRectangle icon;
            if(!altBack) {
                icon = new TargetRectangle(menuIc1.getLeft()+menuIc1.getLength()*5/100, menuIc1.getTop()+ menuIc1.getHeight()*25/100, menuIc1.getLength()*25/100, menuIc1.getHeight()*50/100, blackSt,mattBlackFill);
            }else{
                icon = new TargetRectangle(menuIc1.getLeft()+menuIc1.getLength()*5/100, menuIc1.getTop()+ menuIc1.getHeight()*25/100, menuIc1.getLength()*25/100, menuIc1.getHeight()*50/100, blackSt,whiteFill);
            }*/
            //icon.drawIt(canvas);
            mattBlackFill.setStrokeWidth(2);
            mattBlackFill.setTextSize(55f);
            canvas.drawText("Reset Board", menuIc1.getLeft()+menuIc1.getLength()/2, menuIc1.getTop()+ menuIc1.getHeight()/2,mattBlackFill);
            //blackSt.setStrokeWidth(10);
        }
    }

    private boolean checkTouch(TargetRectangle tr, int tx, int ty){
        if(tx > tr.getLeft() && tx < tr.getRight() && ty < tr.getBottom() && ty > tr.getTop()) {
            return true;
        }else return false;
    }


    public void pause () {
        canDraw = false;
        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    public void resume () {
        canDraw = true;
        thread = new Thread(this);
        thread.start();
    }

    public void updateBigDelta ( double time){
        if (time < 0) {
            time = frameTimeNS - time;
        }
        time += dtPool;
        dtPool = 0;

        while (time >= dt) {
            update(dt);
            time = time - dt;
        }

        dtPool += time;
    }

    public void update ( double time){
        int t = (int) (time / 1000000);
        Log.d("t is :", Integer.toString(t));
        // ballAnimation(1, t);
    }



    public void stats () {
        Log.d("frames per second", Double.toString(frames_per_second));
        Log.d("deltaT", Double.toString(deltaT));
        Log.d("frameTimeS", Double.toString(frameTimeS));
        Log.d("frameTimeMS", Double.toString(frameTimeMS));
        Log.d("frameTimeNS", Double.toString(frameTimeNS));
    }

    public static int getScreenWidth () {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight () {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {

        int e = event.getAction();

        if (e == event.ACTION_DOWN) {
            //canvas.drawCircle(500,500,50,blueSt);
            touchX.add((int) event.getRawX());
            touchY.add((int) event.getRawY());
            Toast myToast = Toast.makeText(getContext(), "Pressed at: " +
                    (int) event.getRawX() + " : " + (int) event.getRawY(), Toast.LENGTH_SHORT);

           // myToast.show();

            int bx = (int) event.getX()/(scrWid/8) +1;
            int by = (int) event.getY()/(scrWid/8);
            if(!menuOpened) {

                if (playerCurrPiece != null) {
                    if (event.getY() > 200 && event.getY() < scrWid + 200) {
                        Move m1 = new Move(playerCurrPiece, playerCurrPiece.getX(), playerCurrPiece.getY(),
                                bx, by, null);//chessBoard.getPiece(bx+1,by+1)));
                        Move m = (Move) currPieceMoveList.contains(m1);

                        if (m != null) {
                            //playerCurrPiece=null;
                            //currPieceMoveList = null;
                            if (movePiece(m)) {
                                //outputText+=m.toString()+"\n";
                                playerCurrPiece = null;
                                currPieceMoveList = null;
                                addTextToOutput(m.toString());
                                botsTurn = true;
                            }
                        } else {
                            playerCurrPiece = null;
                            currPieceMoveList = null;
                        }

                        if (botsTurn) {
                            Move temp = easyAI.makeMove(chessBoard, m);
                            if (temp != null) addTextToOutput(temp.toString());
                            else addTextToOutput("Move was null");

                            botsTurn = false;
                        }
                        //Toast tst = Toast.makeText(getContext(), "::MOVE::", Toast.LENGTH_SHORT);
                        // tst.show();
                    }
                } else if (!botsTurn && event.getY() > 200 && event.getY() < scrWid + 200) {
                    playerCurrPiece = chessBoard.getPiece(bx, by);
                    if (playerCurrPiece != null && !playerCurrPiece.isHuman()) {
                        playerCurrPiece = null;
                        //outputText = "Please select a valid piece!" + "\n";
                        addTextToOutput("Please select a valid piece!");
                        // Toast tst = Toast.makeText(getContext(), outputText, Toast.LENGTH_SHORT);
                        // tst.show();
                        //showOutput(outputFeedbackBox,outputText);
                    }

                    if (playerCurrPiece != null) {
                        currPieceMoveList = playerCurrPiece.getValidMoves(chessBoard);
                        // Toast tst = Toast.makeText(getContext(), "Pressed at: " + playerCurrPiece.getSymbol(), Toast.LENGTH_SHORT);
                        //tst.show();
                    }
                } else {
                    playerCurrPiece = null;
                }
            }
            /*if ((int) event.getRawX() > 550 && (int) event.getRawX() < 850 &&
                    (int) event.getRawY() < 1430 && (int)event.getRawY()>1330) {*//*
            if (checkTouch(textBox, (int) event.getRawX(), (int) event.getRawY())) {

                //stringBuilder = new StringBuilder();
            }

            if(checkTouch(activityLaunch,(int) event.getRawX(), (int) event.getRawY())){


            }
            */
            else if (menuOpened) {
                /*if((int) event.getRawX() > 1 && (int) event.getRawX() < (this.scrWid / 2) / 2
                        && (int) event.getRawY() > 281 && (int)event.getRawY()<281+(800/4)) {*/
                    if (checkTouch(menuIc1, (int) event.getRawX(), (int) event.getRawY())){
                   // altBack = !altBack;
                    this.reset();
                }
            }

           // if ((int) event.getRawX() > 50 && (int) event.getRawX() < 180 && (int) event.getRawY() < 281) {
            if (checkTouch(menuIcBack, (int) event.getX(), (int) event.getY())) {
                menuOpened = true;
            } else menuOpened = false;
        }
        return super.onTouchEvent(event);
    }

    public boolean movePiece(Move move){
        Pieces p = chessBoard.getPiece(move.getSourceX(),move.getSourceY());
        Move m = p.move(move.getDestX(),move.getDestY(),chessBoard); //make the move
        if(m!=null){
            //display.summarizeMove(m);   //if successfully made, summarize the move made
            return true;
        }else{
            System.out.println("Please select a valid Move and try again!");
            return false;
        }//else prompt the user of invalid move
    }

    public void reset(){
        chessBoard.resetBoard();         //reset the board, initialize it and start the game again
        chessBoard.initializeBoard();
        outputText = "";
    }//reset



    public void paints () {
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

        whiteSt = new Paint();
        whiteSt.setColor(Color.WHITE);
        whiteSt.setStyle(Paint.Style.STROKE);
        whiteSt.setStrokeWidth(10);

        greyFill = new Paint();
        greyFill.setColor(Color.rgb(56, 56, 58));
        greyFill.setStyle(Paint.Style.FILL);

        ltPurpleFill = new Paint();
        ltPurpleFill.setColor(Color.rgb(160, 118, 188));
        ltPurpleFill.setStyle(Paint.Style.FILL);

        mattBlackFill = new Paint();
        mattBlackFill.setColor(Color.rgb(29, 31, 35));
        mattBlackFill.setStyle(Paint.Style.FILL);

        shadowColor = new Paint();
        shadowColor.setARGB(80,29, 31, 35);
        mattBlackFill.setStyle(Paint.Style.FILL);

        redSt = new Paint();
        redSt.setColor(Color.RED);
        redSt.setStyle(Paint.Style.STROKE);
        redSt.setStrokeWidth(10);

        darkGreySt = new Paint();
        darkGreySt.setColor(Color.rgb(69, 72, 76));
        darkGreySt.setStyle(Paint.Style.STROKE);
        darkGreySt.setStrokeWidth(10);

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
}

