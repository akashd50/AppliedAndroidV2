/**
 * Created by Akash on 3/7/2018.
 */
package com.example.akash.appliedandroidv2.chessFiles;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Pawn extends Pieces {
    private String name;
    private String symbol;
    private int x,y; //current location on the board
    private boolean isHuman;
    private boolean active;
    private int id;
    public Pawn(int x,int y, boolean human){
        name="Pawn";
        if(human) symbol = "P";
        else symbol = "p";
        this.x = x;
        this.y = y;
        isHuman = human;
        active=true;
        id = tag++;
    }

    public String getSymbol(){
        return symbol;
    }

    public Move move(int xx, int yy, ChessBoard board){
        Move m = new Move(this,x,y,xx,yy,board.getPiece(xx,yy));
        LinkedList l = this.getValidMoves(board);
        Move newMove = (Move)l.contains(m);
        if (newMove!=null) {
            this.x = xx;
            this.y = yy;
            //newMove.toString();
            board.updatePieces(newMove);
            if(!isHuman) System.out.println(newMove.toString());
            return newMove;
        }else {
            //System.out.println("Invalid Move");
            return null;
        }
    }

    public void drawPiece(Canvas c, Paint p, int boardSide){
        p.setStrokeWidth(10);
        p.setTextSize(50f);
        c.drawText(symbol,(x-1)*(boardSide/8)+((boardSide/8)/2),(y)*(boardSide/8)+((boardSide/8)/2),p);
        p.setStrokeWidth(5);
    }

    public LinkedList getValidMoves(ChessBoard board){
        /*
        Returns a list of all the valid moves that this piece can make.
         */

        LinkedList list = new LinkedList();
        if(!this.isHuman) {
            if (y <= 7) {
                int mx = x;
                int my = y + 1;
                Pieces p = board.getPiece(mx, my);
                if (p == null) list.insert(new Move(this, x, y, mx, my, p));
            }
            if (x >= 2 && y <= 7) {
                int mx = x - 1;
                int my = y + 1;
                Pieces p = board.getPiece(mx, my);
                if (p != null && p.isHuman() == !this.isHuman) {
                    list.insert(new Move(this, x, y, mx, my, p));
                }
            }
            if (x <= 7 && y <= 7) {
                int mx = x + 1;
                int my = y + 1;
                Pieces p = board.getPiece(mx, my);
                if (p != null && p.isHuman() == !this.isHuman) {
                    list.insert(new Move(this, x, y, mx, my, p));
                }
            }
        }
        if(this.isHuman) {
            if (y >= 2) {
                int mx = x;
                int my = y - 1;
                Pieces p = board.getPiece(mx, my);
                if (p == null) list.insert(new Move(this, x, y, mx, my, p));
            }
            if (x >= 2 && y >= 2) {
                int mx = x - 1;
                int my = y - 1;
                Pieces p = board.getPiece(mx, my);
                if (p != null && p.isHuman() == !this.isHuman) {
                    list.insert(new Move(this, x, y, mx, my, p));
                }
            }
            if (x <= 7 && y >= 2) {
                int mx = x + 1;
                int my = y - 1;
                Pieces p = board.getPiece(mx, my);
                if (p != null && p.isHuman() == !this.isHuman) {
                    list.insert(new Move(this, x, y, mx, my, p));
                }
            }
        }
        return list;
    }

    public boolean isHuman(){
        return this.isHuman;
    }

    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public void setX(int tX){this.x = tX;}
    public void setY(int tY){this.x = tY;}
    public void setState(boolean act){this.active = act;}
    public boolean getState(){return this.active;}

    public int getId(){return id;}

    public String toString(){
        return this.name;
    }

    public Pieces copy(){
        Pawn temp = new Pawn(this.x,this.y,this.isHuman);
        return temp;
    }

    public boolean pawnPromotionCheck(){
        if(isHuman){
            if(this.y==1){
                return true;
            }
        }
        else{
            if(this.y==8){
                return true;
            }
        }
        return false;
    }
}
