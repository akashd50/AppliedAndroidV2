/**
 * Akashdeep Singh
 * 7802937
 * EasyAI.java extends AI
 * This class provides the implementation of a chess bot with fairly Amateur moves.
 */
package com.example.akash.appliedandroidv2.chessFiles;

public class EasyAI extends AI{
    String name;
    LinkedList aIPieces;

    public EasyAI(String name){
        this.name = name;
        aIPieces = new LinkedList();
    }

    public Move makeMove(ChessBoard board, Move m){
        updatePieces();                                     //update it's pieces before making a move.
        Pieces p = selectRandomPiece();
        if(p!=null) {
            Move toBeMade = selectOptimal(p, board, m);
            while (toBeMade == null) {
                p = selectRandomPiece();
                toBeMade = selectOptimal(p, board, m);
            }
            Move temp = p.move(toBeMade.getDestX(), toBeMade.getDestY(), board);
            return temp;
        }
        return null;
    }//selects random Piece and makes the optimal move.. i.e the move that captures a enemy piece..
    //if it can't be done, makes a random move.

    private Pieces selectRandomPiece(){
        double rand = Math.random()*aIPieces.getLength();
        int randInt = (int)rand;
        Pieces p = (Pieces)aIPieces.startTraversal();
        int count=1;
        while(p!=null && count<aIPieces.getLength()){
            if(count==randInt){
                return p;
            }
            p = (Pieces)aIPieces.traverse();
            count++;
        }
        return null;
    }//selects a random Piece


    public void updatePieces(){
        Pieces p = (Pieces)aIPieces.startTraversal();
        while(p!=null){
            if(!p.getState()){
                aIPieces.remove(p);
            }
            p = (Pieces)aIPieces.traverse();
        }
    }//updates the pieces, in case any pieces are removed from the baord, they must be removed from the List

    public void initializeAI(ChessBoard board) {
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 8; j++) {
                aIPieces.insert(board.getPiece(j,i));
            }
        }
    }//initialize the AI, i.e. gets all it's Pieces from the board.

    public Move selectOptimal(Pieces p, ChessBoard board, Move m){
        LinkedList moveList = p.getValidMoves(board);
        int listLength = 0;
        Move currMove = (Move)moveList.startTraversal();
        while(currMove!=null){
            if(currMove.getTarget()!=null){
                return currMove;
            }
            currMove = (Move)moveList.traverse();
            listLength++;
        }//check if any valid move captures the enemy's piece, if yes returns
        //else selects a random piece in the loop below.

        double rand = Math.random()*listLength;
        int randInt = (int)rand;
        Move move = (Move)moveList.startTraversal();
        int count=0;
        while(p!=null && count<listLength){
            if(count==randInt){
                return move;
            }
            move = (Move)moveList.traverse();
            count++;
        }
        return null;
    }//selects the optimal move for a piece, i.e which captures a enemy piece

}
