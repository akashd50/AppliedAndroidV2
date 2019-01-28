package com.example.akash.appliedandroidv2;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.widget.Toast;

import com.example.akash.appliedandroidv2.Utilities.Utilities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
public class Square {
    private int scrWidth,scrHeight;

    private float left,top, right, bottom;
    private float transformY,transformX;

    private boolean active;

    private float color[];
    private Triangle t1;//= new Triangle(v1,c);
    private Triangle t2;
    private float ar1[],ar2[];
    private Context context;

    private float cx,cy,cz,l,w;

    public Square(float cx,float cy, float cz,float l, float w,float[] c, Context ctx) {
        color = c;
        //sqCoords = vert;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.l = l;
        this.w = w;

        transformY = 0f;
        transformX = 0f;
        active = false;

        float v1[] = {cx- (l/2),cy+ (w/2),cz,
                      cx-(l/2),cy-(w/2),cz,
                        cx+(l/2),cy-(w/2),cz};

        float v2[] = {cx+(l/2),cy-(w/2),cz,
                cx+(l/2),cy+(w/2),cz,
                cx-(l/2),cy+(w/2),cz};

        ar1 = v1;
        ar2 = v2;
        color = c;
        t1 = new Triangle(v1,color);
        t2 = new Triangle(v2,color);

        scrWidth = getScreenWidth();
        scrHeight = getScreenHeight();
        context = ctx;
        convertPointSystem();
    }

    public Square(float cx,float cy, float cz,float l, float w,float[] c, Context ctx, String type) {
        color = c;
        //sqCoords = vert;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.l = l;
        this.w = w;

        transformY = 0f;
        transformX = 0f;
        active = false;

        if(type.compareTo("Z")==0){
            setUpParmsReg(cx,cy,cz,l,w,c);
        }else if(type.compareTo("X")==0) {
            setUpParmsFX(cx, cy, cz, l, w, c);
        }else if(type.compareTo("Y")==0) {
            setUpParmsFY(cx, cy, cz, l, w, c);
        }

        /*float v1[] = {cx- (l/2),cy+ (w/2),cz,
                      cx-(l/2),cy-(w/2),cz,
                        cx+(l/2),cy-(w/2),cz};

        float v2[] = {cx+(l/2),cy-(w/2),cz,
                cx+(l/2),cy+(w/2),cz,
                cx-(l/2),cy+(w/2),cz};

        ar1 = v1;
        ar2 = v2;*/
        color = c;
        /*t1 = new Triangle(v1,color);
        t2 = new Triangle(v2,color);
*/
        scrWidth = getScreenWidth();
        scrHeight = getScreenHeight();
        context = ctx;
        convertPointSystem();
    }



    private void setUpParmsReg(float cx,float cy, float cz,float l, float w,float[] c1){
        float v1[] = {cx- (l/2),cy+ (w/2),cz,
                cx-(l/2),cy-(w/2),cz,
                cx+(l/2),cy-(w/2),cz};

        float v2[] = {cx+(l/2),cy-(w/2),cz,
                cx+(l/2),cy+(w/2),cz,
                cx-(l/2),cy+(w/2),cz};

        ar1 = v1;
        ar2 = v2;

        t1 = new Triangle(v1,c1);
        t2 = new Triangle(v2,c1);

    }

    private void setUpParmsFX(float cx,float cy, float cz,float l, float w,float[] c1){
        float v1[] = {cx- (l/2),cy,cz-w/2,
                cx-(l/2),cy,cz+w/2,
                cx+(l/2),cy,cz+w/2};

        float v2[] = {cx+(l/2),cy,cz+w/2,
                cx+(l/2),cy,cz-w/2,
                cx-(l/2),cy,cz-w/2};

        ar1 = v1;
        ar2 = v2;

        t1 = new Triangle(v1,c1);
        t2 = new Triangle(v2,c1);
    }

    private void setUpParmsFY(float cx,float cy, float cz,float l, float w,float[] c1){
        float v1[] = {cx,cy+w/2,cz+l/2,
                cx,cy-w/2,cz+l/2,
                cx,cy-w/2,cz-l/2};

        float v2[] = {cx,cy-w/2,cz-l/2,
                cx,cy+w/2,cz-l/2,
                cx,cy+w/2,cz+l/2};

        ar1 = v1;
        ar2 = v2;

        t1 = new Triangle(v1,c1);
        t2 = new Triangle(v2,c1);
    }


    public void draw(float[] mvpMatrix) {
        float[] scratcht = new float[16];
        float[] tempMoveMat = new float[16];
        Matrix.setIdentityM(tempMoveMat, 0);
        Matrix.translateM(tempMoveMat, 0, transformX, transformY, 0f);
        Matrix.multiplyMM(scratcht, 0, tempMoveMat, 0, mvpMatrix, 0);
       t1.draw(scratcht);
       t2.draw(scratcht);
    }

    public void convertPointSystem(){
        this.left = (scrWidth/2 + ar1[0]*scrWidth/2) + (this.transformX*(scrWidth/2));
        this.top =  (scrHeight/2-((ar1[1])*scrHeight/2) - ((this.transformY/1.0f) * scrHeight/2));
        this.right = (scrWidth/2 + ar2[0]*scrWidth/2) + (this.transformX*(scrWidth/2));
       // this.bottom = (int) (scrHeight/2- ar2[1]*scrHeight/2);
        this.bottom = (scrHeight/2-((ar2[1])*scrHeight/2) - ((this.transformY/1.0f)* scrHeight/2));
    }

    public boolean isClicked(float tx, float ty){
        /*Toast m = Toast.makeText(context,"Left: "+this.left+" Right: "+this.right+
                " Top: "+this.top+" Bottom: "+this.bottom,Toast.LENGTH_SHORT);
        m.show();*/
        //if(tx - (this.transformX*(scrWidth/2)) > left && tx -(this.transformX*(scrWidth/2)) < right && ty < bottom && ty > top) {
        if(tx > left && tx < right && ty < bottom && ty > top) {
            //Toast.makeText(context,"top: "+top +" bttom: "+bottom,Toast.LENGTH_SHORT).show();
            return true;
        }else return false;
    }

    public static int getScreenWidth () {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight () {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void updateTrasnformY(float vy){
        transformY+=vy;
        convertPointSystem();
    }
    public float getTransformY(){return this.transformY;}

    public void updateTrasnformX(float vx){
        transformX+=vx;
        convertPointSystem();
    }
    public void changeTrasnformX(float vx){
        transformX =vx;
        convertPointSystem();
    }
    public void changeTrasnformY(float vx){
        transformY =vx;
        convertPointSystem();
    }
    public float getTransformX(){return this.transformX;}

    public void deactivate(){
        active = false;
        transformY = 0;
        transformX = 0;
        //transformY = 0f;
    }
    public void activate(){active = true;}

    public boolean isActive(){return this.active;}

    public float getCX(){return this.cx;}
    public float getCY(){return this.cy;}
    public float getCZ(){return this.cz;}

    public float getTop(){return this.top;}
    public float getBottom(){return this.bottom;}

    public float getCenterX(){return (this.left+this.right)/2;}
    public float getCenterY(){return (this.top+this.bottom)/2;}
}
