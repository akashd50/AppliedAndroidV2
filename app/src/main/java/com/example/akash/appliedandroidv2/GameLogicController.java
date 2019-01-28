package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.opengl.Matrix;

public class GameLogicController {
    private int currUserShipLane;
    private Spaceship userShip;
    private Context context;
    private TouchController tc;
    private float currShipLoxX;
    private final float[] shipLocsX = {-0.6f,0.0f,0.6f};

    public GameLogicController(Context ctx, TouchController tcc){
        context = ctx;
        this.tc = tcc;
        currShipLoxX =0f;
        currUserShipLane = 1;

    }

    public void drawOnScreen(){

    }

    public void uslShiftLeft(){
        if(currUserShipLane>=1&& tc.getSwipeFlag()) {
            currUserShipLane -= 1;
            currShipLoxX =shipLocsX[currUserShipLane];
            tc.setSwipeCheckFlag(false);
        }
    }
    public void uslShiftRight(){
        if(currUserShipLane<=1 && tc.getSwipeFlag()) {
            currUserShipLane += 1;
            currShipLoxX = shipLocsX[currUserShipLane];
            tc.setSwipeCheckFlag(false);
        }
    }

    public float getCurrShipLoxX(){return this.currShipLoxX;}

}
