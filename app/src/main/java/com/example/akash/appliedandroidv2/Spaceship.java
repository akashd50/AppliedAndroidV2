package com.example.akash.appliedandroidv2;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.widget.Space;
import android.widget.Toast;

public class Spaceship {

    private FanRect bulletShot = null;
    private final float bulletVelY = 0.050f;
    private final int NUM_BULLETS = 20;
    private FanRect[] shots;
    private int numBulletsAct = 0;
    private int bulletsGone = 0;
    private final int fireRate = 30;
    private int frCounter=0;

    private float currRotationAngle = 0f;
    private float targetAngle = 45f;

    private float targetRotationFrame, currMovingFrame;
    private boolean moveLeft,moveRight;
    private float currShipLoxX;
    private int currUserShipLane;


    private final float[] shipLocsX = {-0.6f,0.0f,0.6f};

    private float screenTop;

    private int scrWidth,scrHeight;

    private float left,top, right, bottom;
    private float transformY,transformX;
    private Context context;

    private float x,y,z;
    private float[] color;
    private float length, height;

    private boolean active, rotLeft, rotRight,rotForward,rotBackward;
    //private float leftWing[], rightWing[], front[];

    private Square cSquare;
    private SpecializedTriangle leftWing, rightWing, front;
    private SpecializedTriangle leftWing1, rightWing1;

    private Square backgroundSquare;
    //---------------------------------------------------
    private SpecializedTriangle flame1L;
    private SpecializedTriangle flame1R;
    private SpecializedTriangle flame3L;
    private SpecializedTriangle flame3R;

    private int numShipsDestroyed, livesRemaining;
    public static int USER_LIVES=5;
    private int fCycle = 1;
    //private SpecializedTriangle leftWW1;

    public Spaceship(float cx, float cy, float cz, float l, float h, float[] sCol, float[] frCol, Context context){
        transformY = 0f;
        transformX = 0f;
        context = context;
        x=cx; y=cy; z=cz;
        length = l;
        height = h;
        color = sCol;

        float sqLen = (float)(0.4)*length;
        float sqHi = (float)(0.4)*height;
        cSquare = new Square(x,y,z,sqLen,sqHi,color,context);

        float frHi = (float)(0.2)*height;
        float fr[] = {x-sqLen/2, y - sqHi/2, z,
                        x,y-(sqHi/2)-frHi, z,
                        x+sqLen/2,y-sqHi/2,z};

        float lh = (float)(0.6)*height;
        float lw[] = {x-length/2,y+lh,z,
                        x-sqLen/2,y-sqHi/2,z,
                        x-sqLen/2,y+sqHi/2,z};

        float[] rw = {x+length/2,y+lh,z,
                x+sqLen/2,y-sqHi/2,z,
                x+sqLen/2,y+sqHi/2,z};

    }

    public Spaceship(float cx, float cy, float cz, float l, float h,
                     boolean player, Context context){
        numShipsDestroyed = 0;
        targetRotationFrame = 10;
        moveLeft = false;
        moveRight = false;
        currUserShipLane = 1;
        currMovingFrame = 0.0f;
        livesRemaining = USER_LIVES;

        scrHeight = getScreenHeight();
        scrWidth = getScreenWidth();
        screenTop = (scrHeight - 80f)/scrWidth;

        transformY = 0f;
        transformX = 0f;
        this.context = context;

        x=cx; y=cy; z=cz;
        length = l;
        height = h;
        float[] c = {0.4f,0.4f,0.4f,1.0f};
        color = c;

        float sqLen = (float)(0.4)*length;
        float sqHi = (float)(0.4)*height;
        cSquare = new Square(x,y,z,sqLen,sqHi,color,context);

        active = false;

        //float frHi = (float)(0.3)*height;
        if(!player) {
            float top = (float)(cy+ (0.7)*height);
            float bottom = (float)(cy-(0.3)*height);
            float left = cx-length/2;
            float right = cx+length/2;

            float lw[] = {x, y, z + 0.1f,1.0f,
                    x, bottom, z,1.0f,
                    right, top, z,1.0f};
            float[] lCol = {229 / 255f, 126 / 255f, 119 / 255f, 1f,
                    150 / 255f, 33 / 255f, 25 / 255f, 1f,
                    130 / 255f, 9 / 255f, 1 / 255f, 1f};

            float lh = (float) (0.2) * height;

            float lw1[] = {x, y + lh, z,1.0f,
                    x, y, z + 0.1f,1.0f,
                    right, top, z,1.0f};
            float[] lCol1 = {153 / 255f, 153 / 255f, 153 / 255f, 1f,
                    237 / 255f, 237 / 255f, 237 / 255f, 1f,
                    173 / 255f, 159 / 255f, 159 / 255f, 1f};

            float[] rw = {left, top, z,1.0f,
                    x, bottom, z,1.0f,
                    x, y, z + 0.1f,1.0f};
            float[] rCol = {130 / 255f, 9 / 255f, 1 / 255f, 1f,
                    150 / 255f, 33 / 255f, 25 / 255f, 1f,
                    229 / 255f, 126 / 255f, 119 / 255f, 1f};

            float[] rw1 = {left, top, z,1.0f,
                    x, y, z + 0.1f,1.0f,
                    x, y + lh, z,1.0f};

            float[] rCol1 = {173 / 255f, 159 / 255f, 159 / 255f, 1f,
                    237 / 255f, 237 / 255f, 237 / 255f, 1f,
                    229 / 255f, 126 / 255f, 119 / 255f, 1f};


            leftWing = new SpecializedTriangle(lw, lCol);
            leftWing1 = new SpecializedTriangle(lw1, lCol1);
            rightWing = new SpecializedTriangle(rw, rCol);
            rightWing1 = new SpecializedTriangle(rw1, rCol1);
            backgroundSquare = new Square(x,y+lh,z,length,height,color,context);
            initializeEnemyFlame();
        }else{
            float top = (float)(cy+ (0.3)*height);
            float bottom = (float)(cy-(0.7)*height);
            float left = cx-length/2;
            float right = cx+length/2;

            float lw[] = {left, bottom, z,1.0f,
                         x, y, z+0.1f,1.0f,
                         x, top, z,1.0f};
            /*float[] lCol = {150 / 255f, 33 / 255f, 25 / 255f, 1f,
                    229 / 255f, 126 / 255f, 119 / 255f, 1f,
                    130 / 255f, 9 / 255f, 1 / 255f, 1f};*/
            float[] lCol = {20/255f, 21/255f, 22/255f,1f,
                    46/255f, 48/255f, 51/255f, 1f,
                    20/255f, 21/255f, 22/255f,1f};

            float lh = (float) (0.2) * height;

            float lw1[] = {left, bottom, z,1.0f,
                    x, y-lh, z,1.0f,
                    x, y, z+0.1f,1.0f};
            float[] lCol1 = {153 / 255f, 153 / 255f, 153 / 255f, 1f,
                    145/255f, 179/255f, 214/255f, 1f,
                    237 / 255f, 237 / 255f, 237 / 255f, 1f};

            float[] rw = {x, top, z,1.0f,
                    x, y, z+0.1f,1.0f,
                    right, bottom, z,1.0f};
            float[] rCol = {20/255f, 21/255f, 22/255f,1f,
                    46/255f, 48/255f, 51/255f, 1f,
                    20/255f, 21/255f, 22/255f,1f
                    };

            float[] rw1 = {x, y, z+0.1f,1.0f,
                    x, y-lh, z,1.0f,
                    right, bottom, z,1.0f};

            float[] rCol1 = {173 / 255f, 159 / 255f, 159 / 255f, 1f,
                     145/255f, 179/255f, 214/255f, 1f,
                    229 / 255f, 126 / 255f, 119 / 255f, 1f};



            leftWing = new SpecializedTriangle(lw, lCol);
            leftWing1 = new SpecializedTriangle(lw1, lCol1);
            rightWing = new SpecializedTriangle(rw, rCol);
            rightWing1 = new SpecializedTriangle(rw1, rCol1);
            backgroundSquare = new Square(x,y-lh,z,length,height,color,context);
            initializeFlameVariables();
        }
        float[] col = new float[3];
        shots = new FanRect[NUM_BULLETS];
        for(int i=0;i<NUM_BULLETS;i++){
            shots[i] = new FanRect(x,y,z,0.02f,0.1f,col,context);
        }


    }

    public void draw(float[] mvpMatrix){
        frCounter++;
        float[] scratcht = new float[16];
        float[] tempMoveMat = new float[16];
        Matrix.setIdentityM(tempMoveMat, 0);
        if(moveLeft && currMovingFrame<targetRotationFrame){
            float tC = 0.6f/targetRotationFrame;
            this.transformX -= tC;
           // Matrix.rotateM(tempMoveMat, 0, 30, 0f, 1f, 0f);
            backgroundSquare.updateTrasnformX(-tC);
            currMovingFrame++;
            rotRight = true;
        }else if(moveRight && currMovingFrame<targetRotationFrame) {
            float tC = 0.6f/targetRotationFrame;
            this.transformX += tC;
            //Matrix.rotateM(tempMoveMat, 0, -30, 0f, 1f, 0f);
            backgroundSquare.updateTrasnformX(tC);
            currMovingFrame++;
            rotLeft = true;
        }else{
            resetRotation();
           // moveLeft = false;
            //moveRight = false;
        }
        Matrix.translateM(tempMoveMat, 0, this.getTransformX(), this.getTransformY(), 0f);
       if(rotRight) {
            if(currRotationAngle<targetAngle) currRotationAngle+=1;
            Matrix.rotateM(tempMoveMat, 0, currRotationAngle, 0f, 1f, 0f);
            //updateTrasnformX(0.1f);
        }else if(rotLeft){
            if(currRotationAngle>-targetAngle) currRotationAngle-=1;
            Matrix.rotateM(tempMoveMat, 0, currRotationAngle, 0f, 1f, 0f);
            //updateTrasnformX(-0.1f);
        }else{
            this.resetRotation();
        }

        //Matrix.translateM(tempMoveMat, 0, 0f, 0f, 0f);
       /*if(rotForward) {
           if(currRotationAngle<targetAngle) currRotationAngle+=0.5f;
           Matrix.rotateM(tempMoveMat, 0, currRotationAngle, 1f, 0f, 0f);
       }*/

       //this.resetRotation();
        Matrix.multiplyMM(scratcht, 0, mvpMatrix, 0, tempMoveMat, 0);

        //Matrix.translateM(scratcht, 0, 0f, 0f, -2.5f);

        if(fCycle<=5) {

            flame3L.draw(scratcht);
            flame3R.draw(scratcht);
            fCycle+=1;
        }else if(fCycle>5 && fCycle<7){

            flame1L.draw(scratcht);
            flame1R.draw(scratcht);
            fCycle+=1;
        }else {
            fCycle = 0;
        }

        leftWing.draw(scratcht);
        leftWing1.draw(scratcht);
        rightWing.draw(scratcht);
        rightWing1.draw(scratcht);
       // backgroundSquare.draw(scratcht);
        for(int i=0;i<NUM_BULLETS;i++) {
            FanRect tempS = shots[i];
            if (!tempS.isActive()) {
                tempS.changeTrasnformX(this.getTransformX());
                tempS.changeTrasnformY(this.getTransformY());
            }
        }
        drawBullets(mvpMatrix);
    }

    public void drawEnemy(float[] mvpMatrix){

        if(fCycle<=5) {

            flame3L.draw(mvpMatrix);
            flame3R.draw(mvpMatrix);
            fCycle+=1;
        }else if(fCycle>5 && fCycle<7){

            flame1L.draw(mvpMatrix);
            flame1R.draw(mvpMatrix);
            fCycle+=1;
        }else {
            fCycle = 0;
        }

        leftWing.draw(mvpMatrix);
        leftWing1.draw(mvpMatrix);
        rightWing.draw(mvpMatrix);
        rightWing1.draw(mvpMatrix);
    }

    public boolean isClicked(float tx, float ty){
        /*Toast m = Toast.makeText(context,"Left: "+this.left+" Right: "+this.right+
                " Top: "+this.top+" Bottom: "+this.bottom,Toast.LENGTH_SHORT);
        m.show();*/

        //if(tx - (this.transformX*(scrWidth/2)) > left && tx -(this.transformX*(scrWidth/2)) < right && ty < bottom && ty > top) {
        if(backgroundSquare.isClicked(tx,ty)) {
            //Toast.makeText(context,"SpaceShip Clicked",Toast.LENGTH_SHORT).show();
            return true;
        }else return false;
    }

    public boolean fire(){
        if(frCounter>=fireRate) {
            if (numBulletsAct >= NUM_BULLETS) {
                numBulletsAct = 0;
                for (int i = 0; i < NUM_BULLETS; i++) {
                    shots[i].deactivate();
                }
                bulletsGone = 0;
            }
            shots[numBulletsAct].activate();
            numBulletsAct++;
            frCounter = 0;

        }else{
            frCounter++;
        }
        return true;
    }

    private void drawBullets(float[] mMVPMatrix){
        if(numBulletsAct>0) {
            for(int i=bulletsGone;i<numBulletsAct;i++) {
                FanRect curr = shots[i];

                if(curr.isActive()) {
                    if (curr.getTransformY() + curr.getCY() >= screenTop) {
                        curr.deactivate();
                        bulletsGone++;
                    }else {
                        float[] scratcht = new float[16];
                        float[] tempMoveMat = new float[16];
                        Matrix.setIdentityM(tempMoveMat, 0);
                        curr.updateTrasnformY(bulletVelY);
                        /*if(rotLeft) {
                            //if(currRotationAngle<targetAngle) currRotationAngle+=5;
                            Matrix.rotateM(tempMoveMat, 0, currRotationAngle, 0f, 1f, 0f);
                            //updateTrasnformX(0.1f);
                        }else if(rotRight){
                            //if(currRotationAngle>-targetAngle) currRotationAngle-=5;
                            Matrix.rotateM(tempMoveMat, 0, currRotationAngle, 0f, 1f, 0f);
                            //updateTrasnformX(-0.1f);
                        }*/
                        Matrix.rotateM(tempMoveMat, 0, 30f, 1f, 0f, 0f);

                        Matrix.translateM(tempMoveMat, 0, curr.getTransformX(), curr.getTransformY(), curr.getCZ());
                        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPMatrix, 0);
                        //bulletShot = bull;
                        curr.draw(scratcht);
                    }
                }
            }
        }
    }

    public boolean onTriggerCollision(Spaceship s){

        for(int i=0;i<NUM_BULLETS;i++){
            FanRect curr = shots[i];
            if(curr.isActive()){
                float tx = (curr.getCX()+curr.getTransformX())*scrWidth/2 + scrWidth/2;
                float ty = (float) (scrHeight/2-((curr.getCY()+curr.getTransformY())*scrHeight/2)/1.7);
                if(s.isClicked(tx,ty)){
                    curr.deactivate();
                    numShipsDestroyed++;
                    return true;
                }
            }
            if(s.isClicked(backgroundSquare.getCenterX(),backgroundSquare.getCenterY())){
                livesRemaining--;
                return true;
            }
        }
        return false;
    }

    public void updateTrasnformY(float vy){
        transformY+=vy;
        backgroundSquare.updateTrasnformY(vy);
    }
    public float getTransformY(){return this.transformY;}

    public void updateTrasnformX(float vx){
        transformX+=vx;
        backgroundSquare.updateTrasnformX(vx);
    }
    public void changeTrasnformX(float vx){
        transformX =vx;
        backgroundSquare.changeTrasnformX(vx);
    }
    public void changeTrasnformY(float vy){
        transformY =vy;
        backgroundSquare.changeTrasnformY(vy);
    }
    public float getTransformX(){return this.transformX;}
    public static int getScreenWidth () {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight () {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public boolean isActive(){return this.active;}
    public void activate(){this.active=true;}
    public void deactivate(){this.active = false;}

    public float getCX(){return this.x;}
    public float getCY(){return this.y;}
    public float getCZ(){return this.z;}

    private void initializeFlameVariables(){
        float f1r = 100/255f;
        float f1G = 162/255f;
        float f1B = 209/255f;
        float[] flCol1 = {f1r, f1G, f1B,0.0f,
                f1r, f1G, f1B,0.0f,
                f1r, f1G, f1B,1.0f};
        float[] flCol2 = {f1r, f1G, f1B,0.0f,
                f1r, f1G, f1B,1.0f,
                f1r, f1G, f1B,0.0f};
        float flameLen = 0.10f;
        float flameHi = 0.23f;
        float flx = x;
        float fly = y-0.07f;

        float[] f1L = {x-flameLen,fly,z,1.0f,
                        flx,fly-flameHi,z,1.0f,
                        flx,fly,z,1.0f};

        float[] f1R = {x+flameLen,fly,z,1.0f,
                        flx,fly,z,1.0f,
                        flx,fly-flameHi,z,1.0f};

        flame1L = new SpecializedTriangle(f1L,flCol1);
        flame1R = new SpecializedTriangle(f1R,flCol2);

        flameLen = 0.09f;
        flameHi = 0.16f;
        flx = x;
        fly = y-0.07f;
        float[] fl3L = {x-flameLen,fly,z,1.0f,
                        flx,fly-flameHi,z,1.0f,
                        flx,fly,z,1.0f};

        float[] fl3R = {x+flameLen,fly,z,1.0f,
                        flx,fly,z,1.0f,
                        flx,fly-flameHi,z,1.0f};

        float[] fl3ColL = {f1r, f1G, f1B,0.0f,
                            f1r, f1G, f1B,0.0f,
                            f1r, f1G, f1B,1.0f};
        float[] fl3ColR = {f1r, f1G, f1B,0.0f,
                            f1r, f1G, f1B,1.0f,
                            f1r, f1G, f1B,0.0f};
        flame3L = new SpecializedTriangle(fl3L,fl3ColL);
        flame3R = new SpecializedTriangle(fl3R,fl3ColR);
    }

    private void initializeEnemyFlame(){
        float f1r = 200/255f;
        float f1G = 50/255f;
        float f1B = 50/255f;
        float[] flCol1 = {f1r, f1G, f1B,0.0f,
                f1r, f1G, f1B,1.0f,
                f1r, f1G, f1B,0.0f};
        float[] flCol2 = {f1r, f1G, f1B,0.0f,
                f1r, f1G, f1B,1.0f,
                f1r, f1G, f1B,0.0f};
        float flameLen = 0.10f;
        float flameHi = 0.23f;
        float flx = x;
        float fly = y+0.07f;

        float[] f1L = {flx-flameLen,fly,z,1.0f,
                        flx,fly,z,1.0f,
                        flx,fly+flameHi,z,1.0f};

        float[] f1R = {flx,fly+flameHi,z,1.0f,
                        flx,fly,z,1.0f,
                        flx+flameLen,fly,z,1.0f};

        flame1L = new SpecializedTriangle(f1L,flCol1);
        flame1R = new SpecializedTriangle(f1R,flCol2);

        flameLen = 0.09f;
        flameHi = 0.16f;
        flx = x;
        fly = y+0.07f;
        float[] fl3L = {flx-flameLen,fly,z,1.0f,
                        flx,fly,z,1.0f,
                        flx,fly+flameHi,z,1.0f};

        float[] fl3R = {flx,fly+flameHi,z,1.0f,
                        flx,fly,z,1.0f,
                        flx+flameLen,fly,z,1.0f};

        float[] fl3ColL = {f1r, f1G, f1B,0.0f,
                f1r, f1G, f1B,1.0f,
                f1r, f1G, f1B,0.0f};
        float[] fl3ColR = {f1r, f1G, f1B,0.0f,
                f1r, f1G, f1B,1.0f,
                f1r, f1G, f1B,0.0f};
        flame3L = new SpecializedTriangle(fl3L,fl3ColL);
        flame3R = new SpecializedTriangle(fl3R,fl3ColR);
    }

    public void rotateLeft(){
        this.rotLeft = true;
        rotRight = false;
        //currRotationAngle = 0f;
    }
    public void rotateRight(){
        this.rotRight = true;
        rotLeft = false;
        //currRotationAngle = 0f;
    }
    public void resetRotation(){
        rotRight = false;
        rotLeft = false;
        rotForward = false;
        rotBackward = false;
        currRotationAngle = 0f;
    }
    public boolean getRotationLeft(){return rotLeft;}
    public boolean getRotationRight(){return rotRight;}
    public void rotateForward(){
        this.rotForward = true;
        if(rotBackward){
            currRotationAngle = 0.0f;
            this.rotBackward = false;
        }

    }
    public void rotateBackward(){
        this.rotBackward = true;
        if(rotForward){
            currRotationAngle = 0.0f;
            this.rotForward = false;
        }
    }

    public void moveLeft(){

        if(currUserShipLane==0) {

        }else{
            moveRight = false;
            //rotLeft = true;
            currMovingFrame = 0f;
            currUserShipLane -= 1;
            moveLeft = true;
            //backgroundSquare.changeTrasnformY();
            //this.transformX = shipLocsX[currUserShipLane];
        }
    }

    public int getCurrUsershipLane(){
        return currUserShipLane;
    }
    public int getNumShipsDestroyed(){return this.numShipsDestroyed;}
    public int getLivesRemaining(){return livesRemaining;}

    public void moveRight(){
        if(currUserShipLane==2) {
        }else{
                moveLeft = false;
                //rotRight = true;
                currMovingFrame = 0f;
                currUserShipLane += 1;
                moveRight = true;
                //this.transformX = shipLocsX[currUserShipLane];
            }
    }
}
