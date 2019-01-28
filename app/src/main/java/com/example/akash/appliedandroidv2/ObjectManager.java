package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.opengl.Matrix;

import com.example.akash.appliedandroidv2.Utilities.Utilities;

import java.util.ArrayList;

public class ObjectManager {

    private ArrayList<Objects> obejcts;
    private Spaceship[] centerLane,leftLane,rightLane;
    private float[] larray = {-0.6f,0.0f,0.6f};
    private float targetFrames =120f;

    private float generationHold = 30f;
    private final int nums = 10;
    private float holdTracker = 0f;

    private float difficultyMeter;
    private Spaceship[][] lanes= {leftLane,centerLane,rightLane};

    public ObjectManager(Context ctx){
        obejcts = new ArrayList<Objects>();
        float[] co = {0.1f,0.4f,0.2f,1.0f};
        float[] co1 = {0.1f,0.1f,0.2f,1.0f};
        float[] co3 = {0.3f,0.4f,0.1f,1.0f};
        difficultyMeter = 0f;

        centerLane = new Spaceship[nums];
        leftLane = new Spaceship[nums];
        rightLane = new Spaceship[nums];

        for(int i=0;i<nums;i++) {
            /*Spaceship temp  = new Spaceship(0.0f, 0.0f, 0.3f, 0.2f, 0.2f, 0.4f, co, co1, co3, ctx);
            int pos = (int)(Math.random()*3);
            temp.changeTrasnformX(larray[pos]);
            temp.changeTrasnformY(1.0f);
            obejcts.add(temp);*/
            Spaceship temp = new Spaceship(0.0f, 0.0f, 0.3f, 0.2f, 0.3f, false, ctx);
            temp.changeTrasnformX(larray[1]);
            temp.changeTrasnformY(1.0f);
            centerLane[i] = temp;

            Spaceship temp2 = new Spaceship(0.0f, 0.0f, 0.3f, 0.2f, 0.3f, false, ctx);
            temp2.changeTrasnformX(larray[0]);
            temp2.changeTrasnformY(1.0f);
            leftLane[i] = temp2;

            Spaceship temp3 = new Spaceship(0.0f, 0.0f, 0.3f, 0.2f, 0.3f, false, ctx);
            temp3.changeTrasnformX(larray[2]);
            temp3.changeTrasnformY(1.0f);
            rightLane[i] = temp3;

        }
            //activateSpaceships(2);

        //lanes[0] = new Spaceship(0.3f,1.0f,0.1f,0.2f,6.0f,0.1f,co,ctx);
        //lanes[1] = new Spaceship(-0.3f,1.0f,0.1f,0.3f,6.0f,0.1f,co,ctx);
    }

    public void addObject(Objects o){
        obejcts.add(o);
    }

    public Objects getObject(int i){
        return obejcts.get(i);
    }

    public void frameCheck(float[] mMVPmatrix){
       // drawLanes(mMVPmatrix);
        generateObstacles();
        drawObstacles(mMVPmatrix);
    }

    private void activateSpaceships(int num){
        for(int i=0;i<num;i++){
            int laneNo = (int)(Math.random()*3);
            int index = (int)(Math.random()*nums);
            Spaceship temp = null;
            if(laneNo==0) {
                temp = leftLane[index];
            }else if(laneNo==1){
                temp = centerLane[index];
            }else if(laneNo==2){
                temp = rightLane[index];
            }
            temp.activate();
        }
    }

    private void generateObstacles(){
        if(holdTracker>= generationHold){
            activateRandom();
           // activateRandom();
            holdTracker=0f;
           /* if(targetFrames>=60){
                targetFrames--;
                //generationHold--;
            }*/
        }else holdTracker++;
    }

    private void activateRandom(){
        int laneNo = (int)(Math.random()*3);
        int index = (int)(Math.random()*nums);
        Spaceship temp = null;
        if(laneNo==0) {
            temp = leftLane[index];
        }else if(laneNo==1){
            temp = centerLane[index];
        }else if(laneNo==2){
            temp = rightLane[index];
        }
        if(temp.isActive()){
            while(!temp.isActive()){
                laneNo = (int)(Math.random()*3);
                index = (int)(Math.random()*nums);
                if(laneNo==0) {
                    temp = leftLane[index];
                }else if(laneNo==1){
                    temp = centerLane[index];
                }else if(laneNo==2){
                    temp = rightLane[index];
                }
            }
        }
        temp.activate();
    }

    private void drawObstacles(float[] mMVPmatrix){
        for(int j=0;j<3;j++) {
            for (int i = 0; i < nums; i++) {
                Spaceship temp = null;
                if(j==0) {
                    temp = leftLane[i];
                }else if(j==1){
                    temp = centerLane[i];
                }else if(j==2){
                    temp = rightLane[i];
                }

                if (temp.isActive()) {
                    temp.updateTrasnformY(-2.0f / targetFrames);
                    if (temp.getTransformY() < Utilities.getScreenBottom()) {
                        temp.changeTrasnformY(1.2f);
                        temp.deactivate();
                        //activateRandom();
                        //activateRandom();
                    }
                    float[] scratcht = new float[16];
                    float[] tempMoveMat = new float[16];
                    Matrix.setIdentityM(tempMoveMat, 0);
                    Matrix.translateM(tempMoveMat, 0, temp.getTransformX(), temp.getTransformY(), 0f);
                    //Matrix.rotateM(tempMoveMat, 0, 45f, 1f, 0f, 0f);

                    Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPmatrix, 0);
                    temp.drawEnemy(scratcht);
                }
            }
        }
    }

    private void drawLanes(float[] mMVPmatrix){
        float[] scratcht = new float[16];
        float[] tempMoveMat = new float[16];
        Matrix.setIdentityM(tempMoveMat, 0);
        Matrix.rotateM(tempMoveMat, 0, 45f, 1f, 0f, 0f);
        //Matrix.rotateM(tempMoveMat, 0, 20f, 0f, 1f, 0f);
        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mMVPmatrix, 0);
    }

    public Spaceship[] getCenterLane(){return this.centerLane;}
    public Spaceship[] getLeftLane(){return this.leftLane;}
    public Spaceship[] getRightLane(){return this.rightLane;}
}
