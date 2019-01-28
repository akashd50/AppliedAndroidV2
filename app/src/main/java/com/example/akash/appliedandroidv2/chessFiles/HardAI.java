/**
 * Akashdeep Singh
 * 7802937
 * HardAI.java extends AI
 * This class provides the implementation of a chess bot with some logic behind their moves.
 */

package com.example.akash.appliedandroidv2.chessFiles;

public class HardAI extends AI {
    String name;                //Ai's name
    LinkedList aIPieces;        //Ai's pieces
    LinkedList playerPieces;    //Player's pieces, moved so far

    public HardAI(String name){
        this.name = name;
        aIPieces = new LinkedList();
        playerPieces = new LinkedList();
    }//constructor


    public Move makeMove(ChessBoard board, Move m){
        /*
            This method makes the moves for the AI's pieces.
            It first checks if any of it's important pieces are in danger, i.e anything except the pawns
            if yes, move it out of the way
            Otherwise select the best move from the rest of it's pieces which captures the opponent's pieces
            Or if none is possible, take a guess and play a piece
         */
        updatePieces();                                     //update it's pieces to remove any pieces that aren't on the board
                                                            // before making a move.

        playerPieces.insert(m.getPiece());                  //inset the player piece for tracking
        LinkedList unsafePieces = unsafePieces(board);      //pieces in danger
        Move uMove = (Move)unsafePieces.startTraversal();
        int priority = 999;
        Pieces uPiece = null;
        Pieces playerPiece = null;
        while(uMove!=null){
            if(uMove.getTarget().getId()<priority){
                priority = uMove.getTarget().getId();
                uPiece = uMove.getTarget();
                playerPiece = uMove.getPiece();
            }
            uMove = (Move)unsafePieces.traverse();
        }                                                   //while loop gets the most important piece in danger

        if(uPiece!=null){
            LinkedList uList = uPiece.getValidMoves(board);
            uMove = (Move)uList.startTraversal();
            while(uMove!=null){
                if(uMove.getTarget()!=null && uMove.getTarget().compareTo(playerPiece)){
                    Move temp = uPiece.move(uMove.getDestX(),uMove.getDestY(),board);
                    return temp;
                }
                uMove = (Move)uList.traverse();
            }
        }//check if the piece in danger can capture the enemy's piece, if yes do it
            //otherwise make a random move with that piece and hope it can get out of the way.
        if(uPiece!=null && uPiece.getId()<16) {
            uMove = selectRandomMove(uPiece, board);
            /*if(uMove!=null && uPiece.move(uMove.getDestX(), uMove.getDestY(), board) != null) {
                return true;
            }*/
            if(uMove!=null) {
                Move temp = uPiece.move(uMove.getDestX(), uMove.getDestY(), board);
                if (temp != null) {
                    return temp;
                }
            }
        }//move that piece out of the way, if possible

        LinkedList optimalMoves = getOptimalMoves(board);       //otherwise get all the optimal moves that can be made
        Move ml = (Move)optimalMoves.startTraversal();
        Move optMove = null;
        int optId = 999;
        while(ml!=null){
            if(ml.getTarget().getId()<optId){
                optMove = ml;
            }
            ml = (Move)optimalMoves.traverse();
        }//select the one that captures the most important enemy piece

        if(optMove!=null){
            Pieces mPiece = optMove.getPiece();
            Move temp = mPiece.move(optMove.getDestX(), optMove.getDestY(),board);
            return temp;
        }//make that move and take out that piece
        else{
            makeRandomMove(board);
            return null; //change it LATER
        }//if nothing can be done, move a random piece
    }

    public boolean makeRandomMove(ChessBoard board){
        Pieces p = selectRandomPiece();
        if(p!=null && p.getState()) {
            Move toBeMade = selectRandomMove(p, board);
            while (toBeMade == null || !p.getState()) {
                p = selectRandomPiece();
                toBeMade = selectRandomMove(p, board);
            }
            p.move(toBeMade.getDestX(), toBeMade.getDestY(), board);
            return true;
        }
        return false;
    }//selects a random piece and makes a random move

    private LinkedList unsafePieces(ChessBoard board){
        LinkedList uPieces = new LinkedList();
        Pieces p = (Pieces)playerPieces.startTraversal();
        while(p!=null){
            if(p.getState()) {
                LinkedList vMoves = p.getValidMoves(board);
                Move m = (Move) vMoves.startTraversal();
                while (m != null) {
                    if (m.getTarget() != null) {
                        uPieces.insert(m);
                    }
                    m = (Move) vMoves.traverse();
                }
            }
            p = (Pieces)playerPieces.traverse();
        }
        return uPieces;
    }//gets the pieces that are in danger from player's pieces that are moved on the board.

    public LinkedList getOptimalMoves(ChessBoard board){
        LinkedList optimal = new LinkedList();
        Pieces pie = (Pieces)aIPieces.startTraversal();
        while(pie!=null){
            LinkedList currMoveSet = pie.getValidMoves(board);
            Move m = (Move)currMoveSet.startTraversal();
            while(m!=null){
                if(m.getTarget()!=null){
                    optimal.insert(m);
                }
                m = (Move)currMoveSet.traverse();
            }
            pie = (Pieces)aIPieces.traverse();
        }
        return optimal;
    }//selects an optimal move for the pieces still remaining

    public LinkedList getOptimalValidMoves(ChessBoard board, Pieces p){
        LinkedList optimal = new LinkedList();
        //Pieces pie = (Pieces)aIPieces.startTraversal();
        LinkedList moves = p.getValidMoves(board);
        Move m = (Move)moves.startTraversal();
        while(m!=null){
            if(m.getDestY()>p.getY()){
                optimal.insert(m);
            }
            m = (Move)moves.traverse();
        }
        return optimal;
    }

    public Move getBestPredictiveMove(ChessBoard board, Pieces p){
       // LinkedList bestPredictedMoves = new LinkedList();
        Move bestCurrMove = null;
        int bestId = 999;
        LinkedList currMoves = p.getValidMoves(board);
        Move m = (Move)currMoves.startTraversal();
        while(m!=null){
            Pieces temp = p.copy();
            temp.setX(m.getDestX());
            temp.setY(m.getDestY());
            LinkedList nextMoves = temp.getValidMoves(board);
            Move m2 = (Move)nextMoves.startTraversal();
            while(m2!=null && m2.getTarget()!=null){
                if(m2.getTarget().getId()<bestId){
                    //bestPredictedMoves.insert(m2);
                    bestCurrMove = m;
                    bestId = m2.getTarget().getId();
                }
                m2 = (Move)nextMoves.traverse();
            }
        }
        return bestCurrMove;
    }


    public void updatePieces(){
        //System.out.println("Update pieces");
        Pieces p = (Pieces)aIPieces.startTraversal();
        while(p!=null){
            if(!p.getState()){
                aIPieces.remove(p);
            }
            p = (Pieces)aIPieces.traverse();
        }
    }//updates the state of the Pieces, i.e. if a piece is taken out, it should be removes from the List.

    public void initializeAI(ChessBoard board) {
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 8; j++) {
                aIPieces.insert(board.getPiece(j,i));
            }
        }
    }//initializes the Ai, gets all it's pieces from the board

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
    }//selects a random piece

    private Move selectRandomMove(Pieces p, ChessBoard board){
        LinkedList moveList = p.getValidMoves(board);
        double rand = Math.random()*moveList.getLength();
        int randInt = (int)rand;
        Move move = (Move)moveList.startTraversal();
        int count=0;
        while(p!=null && count<moveList.getLength()){
            if(count==randInt){
                return move;
            }
            move = (Move)moveList.traverse();
            count++;
        }
        return null;
    }//selects a random Move
}
