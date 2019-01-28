package com.example.akash.appliedandroidv2.chessFiles;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.akash.appliedandroidv2.TargetRectangle;

/**
 * Created by Akash on 3/7/2018.
 */
public class ChessBoard{
    Pieces[][] board;
    TargetRectangle boardBackGround;
    private int boardSide;
    private Paint outLine, miniBoxColor, backgroundPaint;

    public ChessBoard(int side, Paint backPaint, Paint alt1, Paint alt2){
        board = new Pieces[8][8];
        boardSide = side;
        outLine = alt1;
        miniBoxColor = alt2;
        backgroundPaint = backPaint;
    }

    public Pieces getPiece(int x, int y){
        return board[y-1][x-1];
    }

    public void updatePieces(Move m){
        board[m.getSourceY()-1][m.getSourceX()-1] = null;
        if(m.getTarget()==null){
            board[m.getDestY()-1][m.getDestX()-1] = m.getPiece();
           // System.out.println(m.toString());
        }else{
            Pieces tbr = m.getTarget(); //piece to be removed, set it's state to false.
            tbr.setState(false);
            board[m.getDestY()-1][m.getDestX()-1] = m.getPiece();
           //2 System.out.println(m.toString());
        }
    }

    public void printBoard(){
        System.out.println("   1   2   3   4   5   6   7   8  ");
        System.out.println("------------------------------------");
        for(int i=0;i<board.length;i++){
            System.out.print(i+1+"| ");
            for(int j=0;j<8;j++){
                Pieces p = board[i][j];
                if(p!=null) System.out.print(board[i][j].getSymbol()+" | ");
                else System.out.print("  | ");

            }
            System.out.print("\n");
            System.out.println("------------------------------------");
        }
        System.out.print("\n");
    }

    public int getBoardSide(){
        return this.boardSide;
    }

    public void drawBoard(Canvas c, Pieces currPiece, LinkedList tempMoveList, int top){
        //boardBackGround = new TargetRectangle(0, 200,boardSide,boardSide,outLine,backgroundPaint);
        //boardBackGround.drawIt(c);
        //LinkedList tempMoveList = moveList;

        /*if(currPiece!=null) {
             tempMoveList = currPiece.getValidMoves(this);
        }*/
        outLine.setStrokeWidth(5);
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(currPiece!=null) {
                    Move tempMove = new Move(currPiece, currPiece.getX(), currPiece.getY(), j+1, i+1, board[i][j]);
                    Move corrMove = (Move) tempMoveList.contains(tempMove);
                    if(corrMove!=null){
                        TargetRectangle temp = new TargetRectangle(j*(boardSide/8), i*(boardSide/8)+top,
                                boardSide/8,boardSide/8,outLine,backgroundPaint);
                        temp.drawIt(c);

                    }else{
                        TargetRectangle temp = new TargetRectangle(j * (boardSide / 8), top + i * (boardSide / 8),
                                boardSide / 8, boardSide / 8, outLine, miniBoxColor);
                        temp.drawIt(c);
                    }
                }else {
                    TargetRectangle temp = new TargetRectangle(j * (boardSide / 8), top + i * (boardSide / 8),
                            boardSide / 8, boardSide / 8, outLine, miniBoxColor);
                    temp.drawIt(c);
                    /*if (board[i][j] != null) {
                        //if (board[i][j].getSymbol().equals("P") || board[i][j].getSymbol().equals("p")) {
                        board[i][j].drawPiece(c, outLine, boardSide);
                        //c.drawText("P",i*boardSide/8,j*boardSide/8,outLine);
                        //}
                    }*/
                }
                if (board[i][j] != null) {
                    //if (board[i][j].getSymbol().equals("P") || board[i][j].getSymbol().equals("p")) {
                    board[i][j].drawPiece(c, outLine, boardSide);
                    //c.drawText("P",i*boardSide/8,j*boardSide/8,outLine);
                    //}
                }
            }
        }
        //outLine.setStrokeWidth(1);
    }

    public void drawPiece(Pieces p){
        board[p.getY()-1][p.getX()-1] = p;
    }

    public void initializeBoard(){

        drawPiece(new King(5,1,false));
        drawPiece(new King(5,8,true));

        drawPiece(new Queen(4,1,false));
        drawPiece(new Queen(4,8,true));

        drawPiece(new Bishop(2,1,false));
        drawPiece(new Bishop(7,1,false));

        drawPiece(new Bishop(2,8,true));
        drawPiece(new Bishop(7,8,true));

        drawPiece(new Rook(1,1,false));
        drawPiece(new Rook(1,8,true));
        drawPiece(new Rook(8,1,false));
        drawPiece(new Rook(8,8,true));

        drawPiece(new Knight(3,1, false));
        drawPiece(new Knight(6,1, false));

        drawPiece(new Knight(3,8, true));
        drawPiece(new Knight(6,8, true));
        for(int i=1;i<=8;i++) {
            drawPiece(new Pawn(i, 2, false));
            drawPiece(new Pawn(i, 7, true));
        }
    }

    public void resetBoard(){
        board = new Pieces[8][8];
    }

}
