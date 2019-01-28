package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

public class SurfaveViewTextApp extends SurfaceView implements Runnable {

    SurfaceHolder surfaceHolder;
    boolean canDraw = false;
    Canvas canvas;
    Thread thread = null;
    GameLoop glHelperVar;

    TargetRectangle textBox,instBack,background,activityLaunch,tbarBack, menuBack, menuIcBack,
            menuIc1, warningDialog = null;

    private Bitmap warningBox;

    int scrWid = this.getScreenWidth();
    int scrHi = this.getScreenHeight();

    double frames_per_second, frameTimeS, frameTimeMS, frameTimeNS, physicsRate, dt, dtPool;
    double lastFrame, endOfRender, deltaT;

    ArrayList<Integer> touchX;
    ArrayList<Integer> touchY;

    private int sX, sY, sLoc;

    int bx, by;
    Path square;
    Paint redSt, greenSt, blueSt, blackSt, skBlueSt, darkGreySt, whiteSt;
    Paint redFill, blueFill, greenFill, whiteFill, greyFill, ltPurpleFill, mattBlackFill, shadowColor;
    /*Alien alien;
    Rect alienR;
    Paint currBackgroundColor;
    Paint prevBackgroundColor;*/

    private boolean menuOpened = false;
    private boolean altBack = false;
    InputMethodManager imm;
    StringBuilder stringBuilder;
    //TextView value;
    String ch = "";
    Context super_CX;

    private int instClicks = 0;


    private int stage = 0;
    private boolean emptyBoxPrompt = false;
    private boolean ebpSecondPhase = false;
    private int ebpFrameC = 0;


    private boolean menuClickedAnim = false;
    private int targetFrames = 5;
    private int currFrame = 0;
    //stages:
    // 0 = initialization
    // 1 = instructons
    // 2 =


    public SurfaveViewTextApp(Context context) {
        super(context);
        glHelperVar = new GameLoop();

        super_CX = context;
        surfaceHolder = getHolder();
        canDraw = false;
        sX = 130;
        sY = 400;
        sLoc = 0;
        touchX = new ArrayList<Integer>();
        touchY = new ArrayList<Integer>();

       // bx = 0;
        //by = 0;
        TargetRectangle textBox = new TargetRectangle();
        TargetRectangle instBack = new TargetRectangle();
        TargetRectangle background= new TargetRectangle();
        TargetRectangle activityLaunch= new TargetRectangle();
        TargetRectangle tbarBack= new TargetRectangle();
        TargetRectangle menuBack= new TargetRectangle();
        TargetRectangle menuIc1= new TargetRectangle();

        menuIcBack = new TargetRectangle();

        emptyBoxPrompt = false;

        frames_per_second = 80;
        frameTimeS = 1 / frames_per_second;
        frameTimeMS = frameTimeS * 1000;
        frameTimeNS = frameTimeMS * 1000000;
        dtPool = 0;

        physicsRate = 50;
        dt = (1 / physicsRate) * 1000000000;
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
    public void run () {

    lastFrame = System.nanoTime();
    deltaT = 0;
    paints();

    while (canDraw) {

        if (!surfaceHolder.getSurface().isValid()) {
            continue;
        }
        updateBigDelta(deltaT);
        //draws
        draw2(ch);

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

    public void drawText (String c){
        //canvas = surfaceHolder.lockCanvas();
        blueFill.setTextSize(60f);
        canvas.drawText(c, textBox.getLeft()+textBox.getLength()/2 - (c.length()*10), textBox.getTop()+textBox.getHeight()/2, blueFill);
        //surfaceHolder.unlockCanvasAndPost(canvas);
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
        /*drawInstructions("> Instructions on how the App works:\n" +
                "> Enter a number in the box below to choose the difficulty\n" +
                "> of the chess bot (Either 1 or 2)\n" +
                ">> Go on.. Give it a Try!");
*/
        drawInstructionBox();
        showOutput(instBack,"> Instructions on how the App works:\n" +
                "> Enter a number in the box below to choose the difficulty\n" +
                "> of the chess bot (Either 1 or 2)\n" +
                ">> Go on.. Give it a Try!\n");
        //textbox

        drawTextBox();
        drawText(c);
        drawActivityLaunchButton();
        drawMenuAnim();
        checkEmptyPromptDialog();
        //TargetRectangle rounded = new TargetRectangle(400,400,500,500,redSt);
        //rounded.roundedRect(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void checkEmptyPromptDialog(){
        if(emptyBoxPrompt) {
            //TargetRectangle tempShadow = new TargetRectangle(this.scrWid/2, this.scrHi/2, this.scrWid, this.scrHi,shadowColor);
            //tempShadow.drawIt(canvas);
            int cx = scrWid/2 - currFrame*500/targetFrames;
            int cy = scrHi/2-currFrame*350/targetFrames;
            int w = currFrame*1000/targetFrames;
            int h = currFrame*700/targetFrames;

            if(warningDialog!=null && currFrame<targetFrames){
                currFrame++;
                warningDialog = new TargetRectangle(cx,cy,w,h,blackSt,whiteFill);
                warningDialog.drawIt(canvas);
                TargetRectangle dialogUpperBar = new TargetRectangle(cx,cy,w,currFrame*200/targetFrames,blackSt,mattBlackFill);
                dialogUpperBar.drawIt(canvas);

            }else{
                ebpSecondPhase = true;
                ebpSecondPhaseAnim();
                warningDialog = new TargetRectangle(cx,cy,w,h,blackSt,whiteFill);
                warningDialog.drawIt(canvas);
                TargetRectangle dialogUpperBar = new TargetRectangle(cx,cy,w,currFrame*200/targetFrames,blackSt,mattBlackFill);
                dialogUpperBar.drawIt(canvas);
                blackSt.setStrokeWidth(2);
                blackSt.setTextSize(3*w/100 + 4*h/100);
                canvas.drawText("Please Enter A Number!",cx+w*20/100,cy+h*60/100,blackSt);

            }
        }
    }

    private void ebpSecondPhaseAnim(){
        if(ebpSecondPhase && ebpFrameC<targetFrames) {
            int cx = warningDialog.getLeft() - (ebpFrameC*scrWid/2)/targetFrames;
            int cy = warningDialog.getTop() - (ebpFrameC*scrHi/2)/targetFrames;
            int w = (ebpFrameC*scrWid)/targetFrames + warningDialog.getLength();
            int h = ((ebpFrameC*scrHi)/targetFrames + warningDialog.getHeight())*2;
            TargetRectangle tr = new TargetRectangle(cx, cy, w,h,blackSt,whiteSt);
            //tr.drawIt(canvas);
            TargetRectangle tempShadow = new TargetRectangle(this.scrWid/2, this.scrHi/2, tr.getLength(), tr.getHeight(),shadowColor);
            tempShadow.drawIt(canvas);
            ebpFrameC++;
        }else{
            ebpSecondPhase = false;
            TargetRectangle tempShadow = new TargetRectangle(this.scrWid/2, this.scrHi/2, scrWid, scrHi,shadowColor);
            tempShadow.drawIt(canvas);
            //ebpFrameC = 0;
        }
    }


    private void drawTextBox(){
        redSt.setStrokeWidth(5);
        textBox = new TargetRectangle(scrWid/4, scrHi/2, scrWid/2, scrHi/15, redSt, greyFill);
        textBox.drawIt(canvas);
        redSt.setStrokeWidth(10);
    }

    private void drawActivityLaunchButton(){
        blackSt.setStrokeWidth(5);
        activityLaunch = new TargetRectangle(scrWid/3, scrHi/2 + scrHi/6, scrWid/3, scrHi/12, blackSt, whiteSt, greyFill);
        activityLaunch.drawIt(canvas);
        blackSt.setStrokeWidth(10);
    }

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

            menuBack = new TargetRectangle(0, 0, (this.scrWid / 2), this.scrHi, blackSt, whiteFill);
            menuBack.drawRightStroked(canvas);

            mattBlackFill.setStrokeWidth(3);
            mattBlackFill.setTextSize(70f);
            blackSt.setStrokeWidth(5);
            TargetRectangle title = new TargetRectangle(0, 0, (this.scrWid / 2), tbarBack.getHeight(), blackSt, whiteFill);
            title.drawBottomStroked(canvas);
            canvas.drawText("Options", title.getLeft()+title.getLength()*5/100, title.getTop()+ title.getHeight()*80/100,mattBlackFill);

            blackSt.setStrokeWidth(2);
            menuIc1 = new TargetRectangle(0,tbarBack.getHeight(),this.scrWid / 2, scrHi/10,blackSt,whiteFill);
            menuIc1.drawBottomStroked(canvas);

            TargetRectangle icon;
            if(!altBack) {
                icon = new TargetRectangle(menuIc1.getLeft()+menuIc1.getLength()*5/100, menuIc1.getTop()+ menuIc1.getHeight()*25/100, menuIc1.getLength()*25/100, menuIc1.getHeight()*50/100, blackSt,mattBlackFill);
            }else{
                icon = new TargetRectangle(menuIc1.getLeft()+menuIc1.getLength()*5/100, menuIc1.getTop()+ menuIc1.getHeight()*25/100, menuIc1.getLength()*25/100, menuIc1.getHeight()*50/100, blackSt,whiteFill);
            }
            icon.drawIt(canvas);
            mattBlackFill.setStrokeWidth(2);
            mattBlackFill.setTextSize(55f);
            canvas.drawText("Swap", menuIc1.getLeft()+menuIc1.getLength()/2, menuIc1.getTop()+ menuIc1.getHeight()/2,mattBlackFill);
            //blackSt.setStrokeWidth(10);
        }
    }

    /*private void drawInstructions (String inst){
        int x = 130;
        int y = 400;
        instBack = new TargetRectangle(100, 300, 1300, 800, blackSt, ltPurpleFill);
        instBack.drawIt(canvas);
        blackSt.setTextSize(60f);
        blackSt.setStrokeWidth(5);
        for (int i = 0; i < inst.length(); i++) {
            if (inst.charAt(i) != '\n') {
                canvas.drawText(inst.substring(i, i + 1), x, y, blackSt);
                x += 30;
            } else {
                x = 130;
                y += 70;
            }
        }
    }
*/

    private void drawMenuAnim () {
        if(!emptyBoxPrompt) {
            if (menuClickedAnim && currFrame < targetFrames) {
                TargetRectangle shadwBack = new TargetRectangle(this.scrWid / 2, this.scrHi / 2, this.scrWid, this.scrHi, shadowColor);
                shadwBack.drawIt(canvas);

                menuBack = new TargetRectangle(0, 0, (this.scrWid / 2) * currFrame / targetFrames, this.scrHi, blackSt, whiteFill);
                menuBack.drawRightStroked(canvas);
                currFrame++;
            } else if (currFrame >= targetFrames) {
                menuOpened = true;
                drawMenu();
                menuClickedAnim = false;
                //currFrame = 0;
                //blackSt.setStrokeWidth(10);
            }
        }
    }

    private void drawInstructionBox(){
        instBack = new TargetRectangle(scrWid*5/100, tbarBack.getHeight()+ scrHi*5/100, scrWid*90/100, scrHi*30/100, blackSt, ltPurpleFill);
        instBack.drawIt(canvas);
    }

   /* void drawIstructionAnimation(String inst){
        for (int i = 0; i <= sLoc; i++) {
            if (inst.charAt(sLoc) != '\n') {
                canvas.drawText(inst.substring(sLoc, sLoc + 1), sX, sY, blackSt);
                sX += 30;
            } else {
                sX = 130;
                sY += 70;
            }
        }
    }*/

    private void showOutput(TargetRectangle tb,String s){
        //canvas = surfaceHolder.lockCanvas();

        mattBlackFill.setTextSize(50f);
        mattBlackFill.setStrokeWidth(2);
        int iy = tb.getTop()+20;
        if(s.compareTo("")!=0) {
            String temp = "";
            for(int i=0;i<s.length();i++){
                if(s.charAt(i)!='\n'){
                    temp+=s.charAt(i);
                }else{
                    iy +=50;
                    canvas.drawText(temp, tb.getLeft() + 20, iy, mattBlackFill);
                    temp = "";
                }
            }

        }
        // surfaceHolder.unlockCanvasAndPost(canvas);
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
        Log.d("(bx) =", Integer.toString(bx));
        Log.d("(by) =", Integer.toString(by));
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
            /*Toast myToast = Toast.makeText(getContext(), "Pressed at: " +
                    (int) event.getRawX() + " : " + (int) event.getRawY(), Toast.LENGTH_SHORT);

            myToast.show();*/

            /*if ((int) event.getRawX() > 550 && (int) event.getRawX() < 850 &&
                    (int) event.getRawY() < 1430 && (int)event.getRawY()>1330) {*/
            if (!emptyBoxPrompt) {
                if(!menuOpened) {
                    if (checkTouch(textBox, (int) event.getX(), (int) event.getY())) {
                        imm = (InputMethodManager) super_CX.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, 0);
                        //stringBuilder = new StringBuilder();
                    } else if (checkTouch(activityLaunch, (int) event.getX(), (int) event.getY())) {
                        if (ch.compareTo("") == 0) {
                            emptyBoxPrompt = true;
                            warningDialog = new TargetRectangle(scrWid / 2, scrHi / 2, 0, 0, blackSt, greyFill);
                            Toast t = Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT);
                            t.show();
                        } else {
                            Intent canvasScreen = new Intent(this.getContext(), ChessGameActivity.class);
                            this.getContext().startActivity(canvasScreen);
                        }
                    }else if (checkTouch(menuIcBack, (int) event.getX(), (int) event.getY())) {
                        Toast myT = Toast.makeText(getContext(), "Click Detected (Menu)", Toast.LENGTH_SHORT);
                        myT.show();
                        //menuOpened = true;
                        menuClickedAnim = true;
                    } else menuClickedAnim = false;

                }else if (menuOpened) {
                    if (checkTouch(menuIc1, (int) event.getRawX(), (int) event.getRawY())) {
                        altBack = !altBack;
                    }else{
                        menuOpened = false;
                        menuClickedAnim = false;
                        currFrame = 0;
                    }
                }
            } else if (emptyBoxPrompt && checkTouch(warningDialog, (int) event.getX(), (int) event.getY())) {
                emptyBoxPrompt = false;
                currFrame = 0;
                ebpFrameC = 0;
            }
        }

            /*if (menuOpened) {
                    if (checkTouch(menuIc1, (int) event.getRawX(), (int) event.getRawY())){
                    altBack = !altBack;
                }
            }*/

        return super.onTouchEvent(event);
    }


    public void setInput (String s){
        this.ch = s;
    }



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

        whiteSt = new Paint();
        whiteSt.setColor(Color.WHITE);
        whiteSt.setStyle(Paint.Style.STROKE);
        whiteSt.setStrokeWidth(10);

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
        blackSt.setAntiAlias(true);
        blackSt.setShadowLayer(0.5f,1f,1f,10);

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
