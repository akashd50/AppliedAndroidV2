/**
 * Created by Akash on 3/13/2018.
 */
package com.example.akash.appliedandroidv2.chessFiles;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bishop extends Pieces {

    private String name;
    private String symbol;
    private int x;
    private int y; //current location on the board
    private boolean isHuman;
    private boolean active;
    private int id;
    public Bishop(int x,int y, boolean human){
        name="Bishop";
        if(human) symbol = "B";
        else symbol = "b";
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

    public LinkedList getValidMoves(ChessBoard board) {
        LinkedList list = new LinkedList();

        for(int i=1;i<=8;i++){
            int mx = x+i;
            int my = y+i;
            if(mx<=8 && my<=8) {
                Pieces p = board.getPiece(mx, my);
                if(p==null){
                    list.insert(new Move(this,x,y,mx,my,p));
                }else if(p!=null && p.isHuman()  == !this.isHuman ){
                    list.insert(new Move(this,x,y,mx,my,p));
                    break;
                }else break;
            }
        } // first direction \\>

        for(int i=1;i<=8;i++){
            int mx = x-i;
            int my = y+i;
            if(mx>=1 && my<=8) {
                Pieces p = board.getPiece(mx, my);
                if(p==null){
                    list.insert(new Move(this,x,y,mx,my,p));
                }else if(p!=null && p.isHuman()  == !this.isHuman ){
                    list.insert(new Move(this,x,y,mx,my,p));
                    break;
                }else break;
            }
        } //second direction <//

        for(int i=1;i<=8;i++){
            int mx = x-i;
            int my = y-i;
            if(mx>=1 && my>=1) {
                Pieces p = board.getPiece(mx, my);
                if(p==null){
                    list.insert(new Move(this,x,y,mx,my,p));
                }else if(p!=null && p.isHuman()  == !this.isHuman ){
                    list.insert(new Move(this,x,y,mx,my,p));
                    break;
                }else break;
            }
        }//third direction <\\

        for(int i=1;i<=8;i++){
            int mx = x+i;
            int my = y-i;
            if(mx<=8 && my>=1) {
                Pieces p = board.getPiece(mx, my);
                if(p==null){
                    list.insert(new Move(this,x,y,mx,my,p));
                }else if(p!=null && p.isHuman()  == !this.isHuman ){
                    list.insert(new Move(this,x,y,mx,my,p));
                    break;
                }else break;
            }
        }//fourth direction //>
        return list;
    }

    public boolean isHuman(){return this.isHuman;}

    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public void setX(int tX){this.x = tX;}
    public void setY(int tY){this.x = tY;}
    public void setState(boolean act){this.active = act;}
    public boolean getState(){return this.active;}

    public int getId(){return this.id;}

    public String toString(){return name;}

    public Pieces copy(){
        Bishop temp = new Bishop(this.x,this.y,this.isHuman);
        return temp;
    }
}
