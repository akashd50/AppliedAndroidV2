
package com.example.akash.appliedandroidv2.chessFiles;

/**
 * Created by Akash on 3/7/2018.
 */

public class Move extends GameObject{
    private Pieces piece;
    private int sourceX,sourceY;
    private int destX, destY;
    private boolean isHuman;
    private Pieces removed;

    public Move(Pieces p, int sx,int sy,int dx, int dy, Pieces rem){
        piece = p;
        sourceX = sx;
        sourceY = sy;
        destX = dx;
        destY = dy;
       isHuman = p.isHuman();
        removed = rem;
    }

    public Pieces getPiece(){
        return this.piece;
    }

    public Pieces getTarget(){
        return this.removed;
    }

    public int getSourceX(){
        return sourceX;
    }

    public int getSourceY(){
        return sourceY;
    }

    public int getDestX(){
        return destX;
    }

    public int getDestY(){
        return destY;
    }

    public boolean compareTo(GameObject gameObject){
        if(gameObject instanceof Move){
            Move m = (Move)gameObject;
            if(m.getSourceX() ==this.getSourceX() && m.getSourceY()==this.getSourceY() &&
                    m.getDestX()==this.getDestX() && m.getDestY()==this.getDestY()){
                return true;
            }
        }
        return false;
    }

    public boolean isHuman(){
        return isHuman;
    }

    public String toString(){
        String s = "";
        if(isHuman) s+="Human moved ";
        else s+="Bot moved ";

        s+= this.getPiece()+" from ("+ this.getSourceX()+", "+this.getSourceY()+") " +
                "to ("+this.getDestX()+", "+this.getDestY()+")";
        if(this.getTarget()!=null){
            s+=" and removed "+this.getTarget();
        }
        return s;
    }
}
