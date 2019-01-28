package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.EGLConfig.*;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Space;
import android.widget.Toast;

import com.example.akash.appliedandroidv2.Utilities.Utilities;
import com.example.akash.appliedandroidv2.geometry.ObjDecoder;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private float[] moveMatrix = new float[16];
    private float cameraX = 0f;

    private float vx = 0.01f;
    private float vy = 0.005f;
    private float vz = -0.005f;

    private float cx = 0f;
    private float cy = 0f;
    private float cz = 0f;

    private float l =0.5f;
    private float b = 0.5f;

    private boolean fingerOnScreen;
    private TouchController controller;
    private GameLogicController glController;
    private float testLoc = 0.0f;
    //UI variables...............
    private Square pauseButtonBackground,pauseButtonL, pauseButtonR, pauseActivityDialog;
    private SpecializedRect fadeBlackbg;
    private boolean pauseClicked, pauseAnimFlag, pauseClosingFlag;
    private float pauseFCounter =0f;
    private float pauseAnimGoal = 20f;
    private float pauseDialogOffset = 0.7f;
    private boolean gameOver = false;
    private Cube testCube;
    private ObjDecoder cubeDecoder;
    private ObjectManager objectManager;
    //...........................

    private float screenTop,screenBottom;
    private TexturedPlane livesBar;
    //private float nextX, nextY;
    //Bullet shots variables.................................................
    /*private FanRect bulletShot = null;
    private float bulletVelY = 0.050f;
    private static int NUM_BULLETS = 20;
    private FanRect[] shots;
    private int numBulletsAct = 0;
    private int bulletsGone = 0;*/

    //.......................................................................
    //-------------------------------------------------------------
    private float touchNewX,touchNewY;

    private float[] lineCol1 = {100/255f, 162/255f, 209/255f,1.0f,
                                100/255f, 162/255f, 209/255f,0.0f};


    //--------------------------------------------------------------------------
    private float scrWid,scrHeight;
    private boolean sizeFlag = false;

    Triangle triangle1, triangle2;
    Square s;

    private TexturedPlane backPlane, backPlane2;

    private Spaceship userShip, enemyShip1;
    private float shipsPrevLocX = 0.0f;
    private float shipsPrevLocY = 0.0f;
    private float enVelX=0.01f;
    private GlLine scoreBarLine;
    private boolean shipLocChanged;
    private FanRect testObjRect;
    //Colors.........................................................................
    float c1[] = { 0.0f, 0.5f, 0.0f, 1.0f };
    float c2[] = { 0.2f, 0.3f, 0.0f, 1.0f };
    float grey[] = { 158/255f, 158/255f, 158/255f,1.0f};
    float whiteIsh[] = {224/255f, 224/255f, 224/255f,1.0f};
    float skyBlue[] = {21/255f, 165/255f, 181/255f,1.0f};
    float darkRed[] = {142/255f, 20/255f, 7/255f,1.0f};
    //--------------------------------------------------------------------------------

    float tri1[] = {0.0f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f};

    float tri2[] = {0.0f,0.0f,0.5f,
            0.0f,0.5f,0.0f,
            -0.5f,-0.5f,-0.0f};
    private Context context;
    private float statusBarHeight;
    private Square sourceBackground, sourceBackLine, sourceMArea;
    private float sourceX,sourceY,sourceZ,offX;
    private boolean sourceLocChanged;

    private Square exp;

    public GLRenderer(Context ctx){
        this.context = ctx;
        scrWid = this.getScreenWidth();
        scrHeight = this.getScreenHeight();
        statusBarHeight = this.getStatusBarHeight();
        //Toast t = Toast.makeText(context,statusBarHeight+"",Toast.LENGTH_SHORT);
       // t.show();

        screenTop = (scrHeight - statusBarHeight)/scrWid;
        screenBottom = -(scrHeight - statusBarHeight)/scrWid;
        fingerOnScreen = false;
        controller = new TouchController();
        glController = new GameLogicController(context,controller);
        //shots = new FanRect[NUM_BULLETS];
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //cubeDecoder = new ObjDecoder(R.raw.planeship_a,context);
        initializeUserStuff();
        initializeUIVariables();
        Utilities.initialzeTextBms();

        objectManager = new ObjectManager(context);

        sourceX = 0.0f;
        sourceY = 0.0f;
        sourceZ = 0f;
        offX = 0.0f;
        sourceLocChanged = false;
        s = new Square(sourceX,sourceY,sourceZ,0.2f,0.2f,c2, context);
        s.changeTrasnformY(-0.9f);
        sourceBackground = new Square(0.0f,-1.5f, 0f,2.0f,0.2f,c2, context);
        sourceBackLine = new Square(0.0f,-1.5f, 0f,2.0f,0.02f,grey, context);
        exp = new Square(0.0f,0.0f,0.0f,0.4f,0.4f,darkRed,context);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

        float[] scratch = new float[16];
        // Set the camera position (View matrix)
        if(userShip.getCurrUsershipLane()==1) {
            if(cameraX>0.03||cameraX<-0.03){
                if(cameraX>0){
                    cameraX-=0.6f/30;
                }else{
                    cameraX+=0.6f/30;
                }
            }
            Matrix.setLookAtM(mViewMatrix, 0, cameraX, 0, 5.0f,
                    0.0f, 0.0f, 0.0f,
                    0f, 1.0f, 0.0f);
        }else if(userShip.getCurrUsershipLane()==0){
            if(cameraX!=0.6f){
                if(cameraX>0.6){
                    cameraX-=0.6f/30;
                }else{
                    cameraX+=0.6f/30;
                }
            }
            Matrix.setLookAtM(mViewMatrix, 0, cameraX, 0, 5.0f,
                    0.0f, 0.0f, 0.0f,
                    0f, 1.0f, 0.0f);
        }else{
            if(cameraX!=-0.6f){
                if(cameraX>-0.6){
                    cameraX-=0.6f/30;
                }else{
                    cameraX+=0.6f/30;
                }
            }
            Matrix.setLookAtM(mViewMatrix, 0, cameraX, 0, 5.0f,
                    0.0f, 0.0f, 0.0f,
                    0f, 1.0f, 0.0f);
        }
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //pauseButtonBackground.draw(mMVPMatrix);
        backPlane.updateTransformY(-0.005f);
        backPlane2.updateTransformY(-0.005f);

        if(backPlane.getTransformY()<=-3.2f){
            backPlane.changeTransformY(3.2f);
        }
        if(backPlane2.getTransformY()<=-3.2f){
            backPlane2.changeTransformY(3.2f);
        }
        backPlane.draw(mMVPMatrix);
        backPlane2.draw(mMVPMatrix);
        if(!pauseClicked) {

            drawPauseIcon();

            //cubeDecoder.drawTriangles(mMVPMatrix);

            // Create a rotation transformation for the triangle
        /*long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
        Matrix.multiplyMM(scratch,0,mRotationMatrix,0,mMVPMatrix,0);
        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);*/
            //userShip.draw(scratch);

            //Matrix.translateM(moveMatrix,0,cx,cy,cz);
            //s = new Square(0.0f,0.0f, 0f,l,b,c2,context);
            //rightLane.draw(mMVPMatrix);
            objectManager.frameCheck(mMVPMatrix);

            drawLaneBoundaries();
            //drawEnemies();
        /*if(enemyShip1.isActive() ) {
            if (userShip.onTriggerCollision(enemyShip1)) {
                enemyShip1.deactivate();
                //Toast.makeText(context,"HIT",Toast.LENGTH_SHORT).show();
            }
        }*/

            if (controller.checkLeftSwipe() && !fingerOnScreen && controller.getSwipeFlag()) {
                userShip.moveLeft();
                controller.setSwipeCheckFlag(false);

            } else if (controller.checkRightSwipe() && !fingerOnScreen && controller.getSwipeFlag()) {
                userShip.moveRight();
                controller.setSwipeCheckFlag(false);
            }
            s.draw(mMVPMatrix);
            drawUserShip();
            livesBar.draw(mMVPMatrix);
        }
        checkPauseActivity();
        //scoreBarLine.draw(mMVPMatrix);
    }

    private void rotateCube(){
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);

        float[] scratcht = new float[16];
        float[] tempMoveMat = new float[16];
        Matrix.setIdentityM(tempMoveMat, 0);
        //Matrix.translateM(tempMoveMat, 0, 0.0f, 0.0f, 0f);
        //Matrix.rotateM(tempMoveMat, 0, angle, 1f, 0f, 0f);
        //float angle2 = 0.090f * ((int) time);
        Matrix.rotateM(tempMoveMat, 0, angle, 0f, 1f, 0f);

        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPMatrix, 0);
        exp.draw(scratcht);

    }

    private void drawPauseIcon(){
        pauseButtonR.draw(mMVPMatrix);
        pauseButtonL.draw(mMVPMatrix);
        sourceBackLine.draw(mMVPMatrix);
    }

    private void initializeUserStuff(){
        userShip = new Spaceship(0.0f,0.0f,0.0f,0.3f,0.4f,true, context);
        userShip.changeTrasnformY(-0.5f);
        enemyShip1 = new Spaceship(0.0f,1.0f,0.0f,0.3f,0.4f,false, context);
    }

    private void initializeUIVariables(){
        Bitmap bm = null;
        backPlane = new TexturedPlane(0f,0f,-4.0f,5.0f,10.0f,context,R.drawable.spaceback, bm);
        backPlane2 = new TexturedPlane(0f,0f,-4.0f,5.0f,10.0f,context,R.drawable.spaceback, bm);
        backPlane2.translate(0f,3.2f,0f);

        pauseButtonL = new Square(0f,0f,0.0f,0.03f,0.15f,whiteIsh,context);
        pauseButtonL.changeTrasnformX(0.80f);
        pauseButtonL.changeTrasnformY(0.7f);
        pauseButtonR = new Square(0f,0f,0.0f,0.03f,0.15f,whiteIsh,context);
        pauseButtonR.changeTrasnformX(0.86f);
        pauseButtonR.changeTrasnformY(0.7f);
        pauseButtonBackground = new Square(0f,0f,0.0f,0.14f,0.20f,grey,context);
        pauseButtonBackground.changeTrasnformX(0.83f);
        pauseButtonBackground.changeTrasnformY(0.7f);
        pauseActivityDialog = new Square(-pauseDialogOffset,0f,1f,2*pauseDialogOffset +0.2f,3.54f,skyBlue,context);
        pauseActivityDialog.changeTrasnformX(-pauseDialogOffset);
        pauseClicked = false;
        pauseClosingFlag = false;

        livesBar = new TexturedPlane(-0.8f,0.5f,0.0f,0.2f,0.7f,context, R.drawable.lives_bar,bm);

        float[] c1 = {0f,0f,0f,1f,
                0f,0f,0f,1f,
                0f,0f,0f,0f};
        float[] c2 = {0f,0f,0f,0f,
                0f,0f,0f,0f,
                0f,0f,0f,1f};
        fadeBlackbg = new SpecializedRect(0.6f,0.0f,1.0f,1.0f,3.54f,c1,c2,context, "Z");
    }

    private void drawEnemies(){

        /*if(enemyShip1.getCX()+enemyShip1.getTransformX() >= 1.0f) {
            enVelX = -0.01f;
        }else if(enemyShip1.getCX()+enemyShip1.getTransformX()<=-1.0f) {
            enVelX = +0.01f;
        }
        enemyShip1.updateTrasnformX(enVelX);*/
        if(enemyShip1.isActive()) {
            float[] scratcht = new float[16];
            float[] tempMoveMat = new float[16];
            Matrix.setIdentityM(tempMoveMat, 0);
            Matrix.translateM(tempMoveMat, 0, enemyShip1.getTransformX(), 0.0f, 0f);
            Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPMatrix, 0);
            enemyShip1.drawEnemy(scratcht);
        }
    }

    private void drawLaneBoundaries(){
        float[] scratcht = new float[16];
        float[] tempMoveMat = new float[16];
        Matrix.setIdentityM(tempMoveMat, 0);
        //GLES20.glDrawArrays(GLES20.GL_ARR);
        //Matrix.translateM(tempMoveMat,0,0f,0f,0f);
        //long time = SystemClock.uptimeMillis() % 4000L;
       // float angle = 0.090f * ((int) time);

        Matrix.rotateM(tempMoveMat, 0, 45f, 1f, 0f, 0f);
       // Matrix.rotateM(tempMoveMat, 0, angle, 0f, 1f, 0f);
        //Matrix.translateM(tempMoveMat, 0, 0f, 0f, 0f);
        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPMatrix, 0);
    }

    private void drawUserShip(){
        checkCollision();
        if(fingerOnScreen && sourceLocChanged){
            userShip.fire();
            if(touchNewY>-1.4){
                userShip.updateTrasnformY(0.005f);
                userShip.rotateForward();
            }else if(touchNewY<-1.55){
                userShip.updateTrasnformY(-0.005f);
                userShip.rotateBackward();
            }

            if(touchNewY<=-1.3f && touchNewY>screenBottom) {
                s.changeTrasnformY(touchNewY - s.getCY());
            }
        }
        drawText("Destroyed: "+userShip.getNumShipsDestroyed(), 0.0f,0.8f);
        drawText("Lives: "+userShip.getLivesRemaining(), -0.9f,0.8f);
        if(userShip.getLivesRemaining()==0){
            drawText("GAME OVER!",0.0f,0.0f);
            gameOver=true;
        }else {
            userShip.draw(mMVPMatrix);
        }
    }

    public void checkCollision(){
        Spaceship[] temp = objectManager.getCenterLane();
        for(int i=0;i<temp.length;i++){
            if(userShip.onTriggerCollision(temp[i])){
                temp[i].deactivate();
                temp[i].changeTrasnformY(1.0f);
            }
        }
        temp = objectManager.getLeftLane();
        for(int i=0;i<temp.length;i++){
            if(userShip.onTriggerCollision(temp[i])){
                temp[i].deactivate();
                temp[i].changeTrasnformY(1.0f);
            }
        }
        temp = objectManager.getRightLane();
        for(int i=0;i<temp.length;i++){
            if(userShip.onTriggerCollision(temp[i])){
                temp[i].deactivate();
                temp[i].changeTrasnformY(1.0f);
            }
        }
    }


    private void checkPauseActivity(){
        if(pauseClicked && !pauseClosingFlag){
            float[] pauseScr = new float[16];
            float[] pauseTempMt = new float[16];
            Matrix.setIdentityM(pauseTempMt, 0);
            Matrix.translateM(pauseTempMt, 0, pauseActivityDialog.getTransformX(), 0f, 0f);
            Matrix.multiplyMM(pauseScr, 0, pauseTempMt, 0, mMVPMatrix, 0);
            pauseActivityDialog.draw(pauseScr);
            if(pauseFCounter <= pauseAnimGoal && pauseActivityDialog.getTransformX()<0.0f) {
                pauseFCounter += 1f;
                //pauseActivityDialog.changeTrasnformX((float)((pauseFCounter/pauseAnimGoal)*pauseDialogOffset) -pauseDialogOffset);
                pauseActivityDialog.updateTrasnformX((pauseDialogOffset+0.2f)/pauseAnimGoal);
            }else{
                pauseAnimFlag = false;
                fadeBlackbg.draw(mMVPMatrix);
                //pauseClicked = false;
            }
        }else if(pauseClicked && pauseClosingFlag){
            float[] pauseScr = new float[16];
            float[] pauseTempMt = new float[16];
            Matrix.setIdentityM(pauseTempMt, 0);
            Matrix.translateM(pauseTempMt, 0, pauseActivityDialog.getTransformX(), 0f, 0f);
            Matrix.multiplyMM(pauseScr, 0, pauseTempMt, 0, mMVPMatrix, 0);
            pauseActivityDialog.draw(pauseScr);
            if(pauseActivityDialog.getTransformX() > -0.9) {
                //pauseFCounter += 1f;
                //pauseActivityDialog.changeTrasnformX((float)(pauseDialogOffset - (pauseFCounter/pauseAnimGoal)*pauseDialogOffset) -pauseDialogOffset );/
                pauseActivityDialog.updateTrasnformX(-(pauseDialogOffset+0.2f)/pauseAnimGoal);
            }else{
                pauseClosingFlag = false;
                pauseClicked = false;
                pauseFCounter = 0;
                //pauseClicked = false;
            }
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 100);
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

   /* public void setXandY(float x, float y){
        this.nextX = x;
        this.nextY = y;
    }*/

    public boolean checkClicks(MotionEvent event){
        float tx = (int)event.getRawX();
        float ty = (int)event.getRawY();
        fingerOnScreen = true;
        controller.setCurrSwipeX(tx);
        controller.setPrevSwipeX(tx);
        controller.setSwipeCheckFlag(true);
        controller.setTouchX(tx);
        controller.setTouchPrevX(tx);
        userShip.fire();
        if (s.isClicked(tx, ty)) {
            float tempY = (scrHeight/2-ty)*1.7f*(2/scrHeight);
            sourceLocChanged = true;
            userShip.fire();
            return true;
        }else if(pauseButtonBackground.isClicked(tx,ty)){
            sourceLocChanged = false;
            shipLocChanged = false;
           Toast.makeText(context,"Pause clicked ",Toast.LENGTH_SHORT).show();
            pauseClicked = true;
            pauseAnimFlag = true;
        }else if(pauseClicked){
            //Toast.makeText(context,"Checking Clicks",Toast.LENGTH_SHORT).show();
            if(pauseActivityDialog.isClicked(tx,ty)){
             //   Toast.makeText(context,"PauseBackClicked",Toast.LENGTH_SHORT).show();

            }else{
                //pauseClicked = false;
                pauseAnimFlag = false;
                //pauseActivityDialog.changeTrasnformX(-pauseDialogOffset);
                pauseFCounter = 0f;
                pauseClosingFlag = true;
            }
        } /*else if(userShip.isClicked(tx,ty)){
            shipLocChanged = true;
        }*/

        return false;
    }

    public boolean checkMovement(MotionEvent event){
       float tx = event.getRawX();
       float ty = event.getRawY();
        controller.setTouchX(tx);
        if (sourceLocChanged) {
            //s = new Square((tx - scrWid/2)/scrWid/2,-1.5f,0.0f,0.2f,0.2f,c2,context);
            //sourceX =
            float temp = (tx - scrWid / 2) / (scrWid / 2);
            float tempY = (scrHeight/2-ty)*screenTop*(2/scrHeight);
            touchNewX = temp;
            touchNewY = tempY;

            s.changeTrasnformX(temp);

            if(controller.getTouchX()>controller.getTouchPrevX()){
                userShip.rotateRight();
            }else if(controller.getTouchX()<controller.getTouchPrevX()) {
                userShip.rotateLeft();
            }
            userShip.changeTrasnformX(temp);
            shipsPrevLocX = temp;
            controller.setTouchPrevX(tx);
            //flame1L.changeTrasnformX(temp);
            //flame1R.changeTrasnformX(temp);
            return true;
        }else if(shipLocChanged){
            float temp = (tx - scrWid / 2) / (scrWid / 2);
            userShip.changeTrasnformX(temp);
        }
       return false;
    }



    public void actionUpChanges(MotionEvent event){
       float tx = (float)event.getRawX();

       userShip.resetRotation();
       sourceLocChanged = false;
       shipLocChanged = false;
       fingerOnScreen = false;
       s.changeTrasnformY(-0.9f);
       controller.setCurrSwipeX(tx);

    }


    public static int getScreenWidth () {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight () {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public float getStatusBarHeight() {
        float result = 0;
        float resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize((int)resourceId);
        }
        return result;
    }

    private void drawText(String s, float x, float y){
        float nl = x;
        for(int i=0;i<s.length();i++){
            TexturedPlane temp = Utilities.CHARS_ARRAY[(int)s.charAt(i)];
            temp.translate(nl,y,0.0f);
            temp.draw(mMVPMatrix);
            nl+=0.05f;
        }
    }
}
