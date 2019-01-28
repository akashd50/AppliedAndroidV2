package com.example.akash.appliedandroidv2.geometry;

public class Vector3 {

    private float vx,vy,vz;

    public Vector3(float vx,float vy,float vz){
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public float getVx(){return this.vx;}
    public float getVy(){return this.vy;}
    public float getVz(){return this.vz;}
}
