/**
 * Created by Akash on 3/10/2018.
 */
package com.example.akash.appliedandroidv2.chessFiles;


import android.graphics.Canvas;
import android.graphics.Paint;

public class Knight extends Pieces {
    private String name;
    private String symbol;
    private int x;
    private int y; //current location on the board
    private boolean isHuman;
    private boolean active;
    private int id;
    public Knight(int x,int y, boolean human){
        name="Knight";
        if(human) symbol = "N";
        else symbol = "n";
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
        if(x>=2 && y>=3) {
            int mx = x-1;
            int my = y-2;
            Pieces p = board.getPiece(mx,my);
            if (p == null) {
                list.insert(new Move(this, x, y, mx, my, p));
            } else if (p != null && p.isHuman() == !this.isHuman) {
                list.insert(new Move(this, x, y, mx, my, p));
            }
        }
        if(x<=7 && y>=3) {
            int mx = x+1;
            int my = y-2;
            Pieces p = board.getPiece(mx,my);
            if (p == null) {
                list.insert(new Move(this, x, y, mx, my, p));
            } else if (p != null && p.isHuman() == !this.isHuman) {
                list.insert(new Move(this, x, y, mx, my, p));
            }
        }
        if(x<=6 && y>=2){
            int mx = x+2;
            int my = y-1;
            Pieces p = board.getPiece(mx,my);
            if (p == null) {
                list.insert(new Move(this, x, y, mx, my, p));
            } else if (p != null && p.isHuman() == !this.isHuman) {
                list.insert(new Move(this, x, y, mx,my, p));
            }
        }
       if(x<=6 && y<=7) {
           int mx = x + 2;
           int my = y + 1;
           Pieces p = board.getPiece(mx, my);
           if (p == null) {
               list.insert(new Move(this, x, y, mx, my, p));
           } else if (p != null && p.isHuman() == !this.isHuman ) {
               list.insert(new Move(this, x, y, mx, my, p));
           }
       }
        if(x>=3 && y>=2){
            int mx = x-2;
            int my = y-1;
            Pieces p = board.getPiece(mx,my);
            if (p == null) {
                list.insert(new Move(this, x, y, mx, my, p));
            } else if (p != null && p.isHuman() == !this.isHuman) {
                list.insert(new Move(this, x, y, mx,my, p));
            }
        }
        if(x>=3 && y<=7){
            int mx = x-2;
            int my = y+1;
            Pieces p = board.getPiece(mx,my);
            if (p == null) {
                list.insert(new Move(this, x, y, mx, my, p));
            } else if (p != null && p.isHuman()== !this.isHuman) {
                list.insert(new Move(this, x, y, mx,my, p));
            }
        }
        if(x>=2 && y<=6){
            int mx = x-1;
            int my = y+2;
            Pieces p = board.getPiece(mx,my);
            if (p == null) {
                list.insert(new Move(this, x, y, mx, my, p));
            } else if (p != null && p.isHuman() == !this.isHuman) {
                list.insert(new Move(this, x, y, mx,my, p));
            }
        }
        if(x<=7 && y<=6){
            int mx = x+1;
            int my = y+2;
            Pieces p = board.getPiece(mx,my);
            if (p == null) {
                list.insert(new Move(this, x, y, mx, my, p));
            } else if (p != null && p.isHuman() == !this.isHuman) {
                list.insert(new Move(this, x, y, mx,my, p));
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
        return name;
    }

    public Pieces copy(){
        Knight temp = new Knight(this.x,this.y,this.isHuman);
        return temp;
    }
}
