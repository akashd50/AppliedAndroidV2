package com.example.akash.appliedandroidv2;

/**
 * Created by Akash on 1/12/2018.
 */

public abstract class Objects {
    protected boolean active;

    public Objects(){
        this.active = true;
    }

    public Objects(int x, int y){
        this.active = true;
    }

    public void deactivate(){
        this.active = false;
    }

    public boolean getState(){
        return this.active;
    }

    public void draw(float[] mMVPmatrix){
        this.draw(mMVPmatrix);
    }


}
