/**
 * Name: Akashdeep Singh
 * Student no: 7802937
 * Pieces.java
 * Pieces Parent
 */
package com.example.akash.appliedandroidv2.chessFiles;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Pieces extends GameObject{
    protected static int tag=1;
    public abstract Move move(int xx, int yy, ChessBoard board);
    public abstract String getSymbol();
    public abstract boolean isHuman();
    public abstract int getX();
    public abstract int getY();
    public abstract void setX(int tX);
    public abstract void setY(int tY);
    public abstract LinkedList getValidMoves(ChessBoard board);
    public abstract void setState(boolean act);
    public abstract boolean getState();
    public abstract int getId();
    public boolean compareTo(GameObject gameObject){
        if(gameObject instanceof Pieces) {
            Pieces p = (Pieces) gameObject;
            if (p.getId() == this.getId()) {
                return true;
            } else return false;
        }
        return false;
    }
    public void drawPiece(Canvas c, Paint p, int boardside){
        this.drawPiece(c,p,boardside);
    }
    public Pieces copy(){
        return this.copy();
    }
}
