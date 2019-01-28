package com.example.akash.appliedandroidv2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by Akash on 1/16/2018.
 */

public class Alien {
    private int scrWidRef;
    private int scrHiRef;
    private int locX, locY;
    private int mazeX, mazeY;
    private ArrayList<Integer> moveLocations;
    //Bitmap alien;
    Rect alien;
    private int up = 0;
    private  int down = 1;
    private int left = 2;
    private int right = 3;
    private int currDir;
    private int nextX,nextY;
    private int tx;// = 500;
    private int ty;// = 500;
    //private Context context;

    private int dir =0;
    public Alien(int x,int y,int scrWid, int scrH, int tx, int ty){//Context c){
        mazeX = x;
        mazeY = y;
        locX = (int)Math.floor(mazeX*20/scrWid);
        locY = (int)Math.floor(mazeY*34/scrH);
        nextX = mazeX;
        nextY = mazeY;
        scrWidRef = scrWid;
        scrHiRef = scrH;
        moveLocations = new ArrayList<Integer>();
        this.tx = tx;
        this.ty = ty;
        //context = c;
        //alienB = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.abm2);
        //alienB = Bitmap.createScaledBitmap(alienB,72,72,false);
        alien = new Rect(mazeX-30,mazeY-30,mazeX+30,mazeY+30);
        //dir = left;
    }

    public void setBitmap(Bitmap b){
        //this.alien = b;
    }

    public Rect getAlien(){
        return this.alien;
    }

    public int getCurrX(){return this.mazeX;}
    public int getCurrY(){return this.mazeY;}

    public int newDirection(){
        int val;
        val = (int)(Math.random()*moveLocations.size());
        int smallest = 9999;
        for(int i=0;i<moveLocations.size();i++){
            if(moveLocations.get(i)==up) {
                int ny = mazeY - scrWidRef / 20;
                int nx = mazeX;
                nx = tx - nx;
                ny = ty - ny;
                nx = nx * nx;
                ny = ny * ny;
                int res = nx + ny;
                res = (int) Math.sqrt(res);
                if (res < smallest) {
                    smallest = res;
                    val = i;
                }
            }else if(moveLocations.get(i)==down) {
                int ny = mazeY + scrWidRef / 20;
                int nx = mazeX;
                nx = tx - nx;
                ny = ty - ny;
                nx = nx * nx;
                ny = ny * ny;
                int res = nx + ny;
                res = (int) Math.sqrt(res);
                if (res < smallest) {
                    smallest = res;
                    val = i;
                }
            }else if(moveLocations.get(i)==left) {
                int ny = mazeY;
                int nx = mazeX - scrWidRef / 20;
                nx = tx - nx;
                ny = ty - ny;
                nx = nx * nx;
                ny = ny * ny;
                int res = nx + ny;
                res = (int) Math.sqrt(res);
                if (res < smallest) {
                    smallest = res;
                    val = i;
                }
            }else if(moveLocations.get(i)==right) {
                int ny = mazeY;
                int nx = mazeX + scrWidRef/20;
                nx = tx - nx;
                ny = ty - ny;
                nx = nx * nx;
                ny = ny * ny;
                int res = nx + ny;
                res = (int) Math.sqrt(res);
                if (res < smallest) {
                    smallest = res;
                    val = i;
                }
            }
        }
        //nextX = mazeX+scrWidRef;
        //nextY = mazeY;
        return val;
    }

    public ArrayList<Integer> setMoveLocations(int[][] walls){
        moveLocations = new ArrayList<Integer>();
        if(walls[locY-1][locX] == 0){
            moveLocations.add(up);
        }
        if (walls[locY + 1][locX] == 0) {
            moveLocations.add(down);
        }
        if (walls[locY][locX-1] == 0) {
            moveLocations.add(left);
        }
        if (walls[locY][locX + 1] == 0) {
            moveLocations.add(right);
        }
        //Toast myToast = Toast.makeText(context, "Location: "+moveLocations.toString(),Toast.LENGTH_LONG);
        //myToast.show();
        return moveLocations;
    }

    public boolean isTimeToUpdateDirection(){
        if(currDir==up){
            if(mazeY<=nextY) return true;
            else return false;
        }else if(currDir==down){
            if(mazeY>=nextY) return true;
            else return false;
        }
        else if(currDir==left){
            if(mazeX<=nextX) return true;
            else return false;
        }
        else if(currDir==right){
            if(mazeX>=nextX) return true;
            else return false;
        }
        return false;
    }

    public void updateNextLocation(int[][] walls){
        boolean upd = false;
        if(this.isTimeToUpdateDirection()){//mazeX==nextX && mazeY==nextY) {
            setMoveLocations(walls);
            currDir = newDirection();
            currDir = moveLocations.get(currDir);
            //while (!upd) {
                if (currDir == up) {
                    //if (walls[locX][locY - 1] == 0) {
                        nextX = mazeX;
                        nextY = mazeY - scrWidRef/20;
                        locX = locX;
                        locY = locY - 1;
                        upd = true;
                   // }
                } else if (currDir == down) {
                   // if (walls[locX][locY + 1] == 0) {
                        nextX = mazeX;
                        nextY = mazeY + scrWidRef/20;
                        locX = locX;
                        locY = locY + 1;
                        upd = true;
                   // }
                } else if (currDir == left) {
                   // if (walls[locX - 1][locY] == 0) {
                        nextX = mazeX - scrWidRef/20;
                        nextY = mazeY;
                        locX = locX - 1;
                        locY = locY;
                        upd = true;
                   // }
                } else if (currDir == right) {
                    //if (walls[locX + 1][locY] == 0) {
                        nextX = mazeX + scrWidRef/20;
                        nextY = mazeY;
                        locX = locX + 1;
                        locY = locY;
                        upd = true;
                   // }
                } else {
                    //currDir = newDirection();
                }
            }
        //}
    }

    public void updateLocation(int[][] walls){

        //updateNextLocation(walls);
        //}
        if(currDir == right) {
            if(nextX-mazeX<5) mazeX+=nextX-mazeX;
            else mazeX += 5;
        }else if(currDir == left){
            if(mazeX-nextX<5) mazeX-=mazeX-nextX;
            else mazeX -= 5;
        }else if(currDir == up){
            if(mazeY-nextY<5) mazeY-=mazeY-nextY;
            else mazeY += 5;
            //mazeY-=5;//(int)(scrWidRef/20)/60 ;//* 3;
        }else if(currDir == down){
            if(nextY-mazeY<5) mazeY+=nextY-mazeY;
            else mazeY += 5;
            //mazeY+=5;//(int)(scrWidRef/20)/60 ;//* 3;
        }
        alien.set(mazeX-30,mazeY-30,mazeX+30,mazeY+30);
    }
}
