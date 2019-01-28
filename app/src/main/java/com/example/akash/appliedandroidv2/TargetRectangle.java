package com.example.akash.appliedandroidv2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;

/**
 * Created by Akash on 1/12/2018.
 */

public class TargetRectangle extends Objects {
    Rect rect;
    int left, right;
    int top, bottom;
    Paint paint;
    Paint outLinePaint;
    private boolean outLine;
    Paint shadowPaint;
    private boolean shadowPt;
    private Path shadowPath;

    public TargetRectangle(){
        super();
    }

    public TargetRectangle(int posx, int posy, int w, int h, Paint paint){
        super();
        left = posx - w/2;
        right = posx + w/2;
        top = posy - h/2;
        bottom = posy + h/2;

        rect = new Rect();
        rect.set(left, top, right, bottom);
        this.paint = paint;
        this.outLine = false;
        this.shadowPt = false;

        //rect.set
    }

    public TargetRectangle(int cx, int cy, int w, int h, Paint oLPaint, Paint paint){
        super();
        left = cx;
        right = cx+w;
        top = cy;
        bottom  = cy+h;
        this.paint = paint;
        this.outLinePaint = oLPaint;
        this.outLine = true;
        this.shadowPt = false;
        rect = new Rect();
        rect.set(left, top, right, bottom);

    }

    public TargetRectangle(int cx, int cy, int w, int h, Paint oLPaint, Paint shadowP, Paint paint){
        this.shadowPt = true;
        this.outLine = true;
        this.paint = paint;
        this.shadowPaint = shadowP;
        this.outLinePaint = oLPaint;
        left = cx;
        right = cx+w;
        top = cy;
        bottom  = cy+h;
        rect = new Rect();
        rect.set(left, top, right, bottom);
    }



    public void drawIt(Canvas c){
        if(outLine && !shadowPt) {
            outLinePaint.setStrokeWidth(3);
            c.drawRect(rect, paint);
            c.drawRect(rect, outLinePaint);
        }else if(!outLine && !shadowPt){
            c.drawRect(rect, paint);
        }else if(outLine && shadowPt){
            /*c.drawRect(rect, paint);
            c.drawRect(rect, outLinePaint);
            Rect r = new Rect();
            r.set(left+(right-left)/20,top+(bottom-top)/10,right-(right-left)/20, bottom -(bottom-top)/10 );
            c.drawRect(r,shadowPaint);*/
            shadowedRect(c);
        }
    }

    private void shadowedRect(Canvas c){
        c.drawRect(rect,paint);
        shadowPath = new Path();
        shadowPath.moveTo(this.left,this.bottom);
        shadowPath.lineTo(this.left,this.top);
        shadowPath.moveTo(this.left,this.top);
        shadowPath.lineTo(this.right,this.top);
        shadowPaint.setStrokeWidth(5);
        c.drawPath(shadowPath,shadowPaint);

        shadowPath = new Path();
        shadowPath.moveTo(this.right,this.top);
        shadowPath.lineTo(this.right,this.bottom);
        shadowPath.moveTo(this.right,this.bottom);
        shadowPath.lineTo(this.left,this.bottom);
        outLinePaint.setStrokeWidth(5);//(right-left)/80);
        c.drawPath(shadowPath,outLinePaint);

    }

    public void drawRightStroked(Canvas c){
        if(this.outLine){
            c.drawRect(rect, paint);
            Path path = new Path();
            outLinePaint.setStrokeWidth(1);
            path.moveTo(this.right,this.top);
            path.lineTo(this.right,this.bottom);
            c.drawPath(path,outLinePaint);
        }
    }

    public void drawBottomStroked(Canvas c){
        if(this.outLine){
            c.drawRect(rect, paint);
            Path path = new Path();
            //outLinePaint.setStrokeWidth(1);
            path.moveTo(this.left + this.getLength()*5/100,this.bottom);
            path.lineTo(this.right - this.getLength()*5/100,this.bottom);
            c.drawPath(path,outLinePaint);
        }
    }

    public void roundedRect(Canvas c){
        Path p = new Path();
        p.moveTo(left+20,top);
        p.lineTo(right-20,top);

        int x = right-20;
        int y = top;
        p.moveTo(x,y);
        while(x<right && y<top+20){
            x++;
            y++;
            p.lineTo(x,y);
            p.moveTo(x,y);
        }

        p.lineTo(right,bottom-20);
        x = right;
        y = bottom-20;
        p.moveTo(x,y);
        while(x>right-20 && y<bottom){
            x--;
            y++;
            p.lineTo(x,y);
            p.moveTo(x,y);
        }

        p.lineTo(left-20,bottom);
        x = left-20;
        y = bottom;
        p.moveTo(x,y);
        while(x>left && y>bottom-20){
            x--;
            y--;
            p.lineTo(x,y);
            p.moveTo(x,y);
        }

        p.lineTo(left,top-20);
        x = left;
        y = top-20;
        p.moveTo(x,y);
        while(x<left+20 && y>top){
            x++;
            y--;
            p.lineTo(x,y);
            p.moveTo(x,y);
        }

        c.drawPath(p,paint);
    }


    public int getLeft(){return this.left;}
    public int getRight(){return this.right;}
    public int getTop(){return this.top;}
    public int getBottom(){return this.bottom;}
    public int getLength(){return this.right-this.left;}
    public int getHeight(){return this.bottom - this.top;}
}
