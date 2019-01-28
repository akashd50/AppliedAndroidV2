/**
 * Created by Akash on 3/14/2018.
 */
package com.example.akash.appliedandroidv2.chessFiles;

import android.graphics.Canvas;
import android.graphics.Paint;

public class King extends Pieces {
    private String name;
    private String symbol;
    private int x;
    private int y; //current location on the board

    private boolean active;
    private boolean isHuman;
    private int id;
    public King(int x,int y, boolean human){
        name="King";
        if(human) symbol = "K";
        else symbol = "k";
        this.x = x;
        this.y = y;
        isHuman = human;
        active=true;
        id=tag++;
    }

    public String getSymbol() {
        return this.symbol;
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
        int mx,my;
        if(this.x<=7){
            mx = x+1;
            my = y;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        if(this.x>=2){
            mx = x-1;
            my = y;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        if(this.y<=7){
            mx = x;
            my = y+1;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        if(this.y>=2){
            mx = x;
            my = y-1;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        if(this.x<=7 && this.y<=7){
            mx = x+1;
            my = y+1;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        if(this.x>=2 && this.y>=2){
            mx = x-1;
            my = y-1;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        if(this.x>=2 && this.y<=7){
            mx = x-1;
            my = y+1;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        if(this.x<=7 && this.y>=2){
            mx = x+1;
            my = y-1;
            Pieces p = board.getPiece(mx,my);
            if(p==null) {
                list.insert(new Move(this, x, y, mx, my, p));
            }else if(p!=null && p.isHuman()  == !this.isHuman ){
                list.insert(new Move(this,x,y,mx,my,p));
            }
        }
        return list;
    }


    public boolean isHuman(){return this.isHuman;}

    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public void setX(int tX){this.x = tX;}
    public void setY(int tY){this.x = tY;}
    public void setState(boolean act){this.active = act;}
    public boolean getState(){return this.active;}
    public int getId(){return id;}

    public String toString(){return name;}

    public Pieces copy(){
        King temp = new King(this.x,this.y,this.isHuman);
        return temp;
    }
}
