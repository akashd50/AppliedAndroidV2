package com.example.akash.appliedandroidv2;

public class TouchController {
    private float currSwipeX,currSwipeY,prevSwipeX,prevSwipeY;

    private float touchNewY,touchPrevY, touchNewX, touchPrevX; //updated every frame if the finger is moving.

    private long touchDown, touchUp;
    private boolean fingerOnScreen, swipeCheckFlag;

    public TouchController(){
        currSwipeX = 0f;
        currSwipeY =0f;
        prevSwipeX =0f;
        prevSwipeY =0f;
        touchDown = 0;
        touchUp=0;
        swipeCheckFlag = false;
    }


    public void setCurrSwipeX(float sx){
        this.currSwipeX = sx;
    }
    public void setPrevSwipeX(float sx){
        this.prevSwipeX = sx;
    }

    public boolean checkLeftSwipe(){
        if(currSwipeX-prevSwipeX!=0 && currSwipeX-prevSwipeX < -200){return true;}
        else return false;
    }

    public boolean checkRightSwipe(){
        if(currSwipeX-prevSwipeX!=0 && currSwipeX-prevSwipeX > 200){return true;}
        else return false;
    }

    public void setTouchDown(long time){
        this.touchDown = time;
    }

    public void setTouchUp(long time){
        this.touchUp = time;
    }

    public void setTouchX(float tx){
        this.touchNewX = tx;
    }
    public void setTouchPrevX(float tx){
        this.touchPrevX = tx;
    }

    public void setTouchY(float ty){
        this.touchNewX = ty;
    }

    public float getTouchX(){return touchNewX;}
    public float getTouchY(){return touchNewY;}
    public float getTouchPrevX(){return touchPrevX;}

    public boolean isFingerOnScreen(){return this.fingerOnScreen;}

    public void setFingerOnScreen(){this.fingerOnScreen = true;}
    public void setFingerOffScreen(){this.fingerOnScreen = false;}

    public float getCurrSwipeX(){return currSwipeX;}
    public float getPrevSwipeX(){return prevSwipeX;}

    public void setSwipeCheckFlag(boolean flag){this.swipeCheckFlag = flag;}
    public boolean getSwipeFlag(){return this.swipeCheckFlag;}
}
