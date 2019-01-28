package com.example.akash.appliedandroidv2.chessFiles;

import android.graphics.Canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Akashdeep Singh
 * 7802937
 * GameLogic.java
 *
 */
public class GameLogic implements ChessController{
    private ChessBoard board;
   // private Display display;
    private Canvas display;
    public GameLogic(Canvas disp){
        display = disp;
    }

    public boolean movePiece(Move move){
        Pieces p = board.getPiece(move.getSourceX(),move.getSourceY());
        Move m = p.move(move.getDestX(),move.getDestY(),board); //make the move
        if(m!=null){
            //display.summarizeMove(m);   //if successfully made, summarize the move made
            return true;
        }else{
            System.out.println("Please select a valid Move and try again!");
            return false;
        }//else prompt the user of invalid move
    }

    public void reset(){
        board.resetBoard();         //reset the board, initialize it and start the game again
        board.initializeBoard();
        startGame(board);
    }//reset

    public void startGame(ChessBoard board){
        this.board = board; //updates it's board variable
        int diff = 1;//display.promptForOpponentDifficulty(2); //prompts the user for difficulty
        if(diff == 1){
            //initialize easy Ai
            EasyAI jarvis = new EasyAI("Jarvis");
            jarvis.initializeAI(board);
            gameLoop(jarvis);
        }//if 1, initializes the easyAI
        else if(diff==2){
            HardAI gideon = new HardAI("Gideon");
            gideon.initializeAI(board);
            gameLoop(gideon);
        }//else if 2, initializes the hard AI
    }//startGame

    public void gameLoop(AI deepMind){
        while(true) {                                                       //endless gameloop
            //display.displayBoard(board);
            Move move = null;                                               //variable for the curr move
            boolean moveMade = false;                                       //flag to see if the move was made
            while(!moveMade) {                                              //while the move is not made

                String input = "";//display.promptForInput();                    //prompt the user for input
                int[] locations = processInput(input);                      //process the input

                if (locations != null) {                                    //check to see if the input was proper
                    Pieces p = board.getPiece(locations[0], locations[1]);  //get the piece at the location

                    if(p == null) {                                         //if there was nothing at that location
                        System.out.println("Invalid Piece Selected");       //invalid piece selected to move
                        continue;                                           //continue to the next iteration and prompt again
                    }
                    else if(p.isHuman()){                                   //otherwise, check of the piece selected is human's

                        move = new Move(p, locations[0], locations[1],
                                locations[2], locations[3], board.getPiece(locations[2], locations[3]));
                                                                            //create a new Move
                        boolean valid = movePiece(move);                    //make the move and see if it was valid
                        if (valid) {                                        //if valid, check to see if any king was captured
                            if(move.getTarget()!=null) {
                                if (move.getTarget().getId() == 1) {
                                    //display.gameOver(1);
                                }//if
                                else if (move.getTarget().getId() == 2) {
                                   // display.gameOver(2);
                                }//else if
                            }//if
                            moveMade = true;                                    //if the game is still going, set the flag to be true
                                                                                //and let the AI make it's move
                            pawnPromotion(p);                                   //check for pawn promotion.
                        }
                        else{
                            //not a valid move, continue to the next iteration and prompt the player again
                            continue;
                        }//else
                    }//else if
                }//if
                else {//if the input wasn't proper, continue to the next iteration and prompt again
                    System.out.println("Improper format of the Input. Try again!");
                    continue;
                }//else
            }
            if(moveMade) {//if the move was made by human successfully, AI will make it's move
                //System.out.println("AI called");
                deepMind.makeMove(board, move);
            }else{
                break;
            }//else break the loop
        }//while true
    }//GameLoop()

    public int[] processInput(String s){

        String[] locs = s.trim().split(",");
        if(locs.length<4 || locs.length > 4){
            if(Integer.parseInt(s)==0) {
                System.exit(0);
            }//if 0, exit
            else if(Integer.parseInt(s)==1) {
                reset();
            }//if 1 resets the game
            return null;
        }
        else{
            int[] locations = new int[locs.length];

            for(int i=0;i<locs.length;i++){
                locations[i] = Integer.parseInt(locs[i]);
            }
            return locations;
        }//else convert it for the move locations.
    }//process the input from the user.

    public void pawnPromotion(Pieces pawn){
        if(pawn instanceof Pawn){
            pawn = (Pawn)pawn;
            if(((Pawn) pawn).pawnPromotionCheck()){
                String s = "";//display.promptForPawnPromotion();
                if(s.compareTo("Rook") == 0){
                    //Pieces p = new Rook(pawn.getX(),pawn.getY(),true);
                    pawn.setState(false);
                    board.drawPiece(new Rook(pawn.getX(),pawn.getY(),true));
                }
                else if(s.compareTo("Knight") == 0){
                    //Pieces p = new Rook(pawn.getX(),pawn.getY(),true);
                    pawn.setState(false);
                    board.drawPiece(new Knight(pawn.getX(),pawn.getY(),true));
                }
                else if(s.compareTo("Bishop") == 0){
                    //Pieces p = new Rook(pawn.getX(),pawn.getY(),true);
                    pawn.setState(false);
                    board.drawPiece(new Bishop(pawn.getX(),pawn.getY(),true));
                }else if(s.compareTo("Queen") == 0){
                    //Pieces p = new Rook(pawn.getX(),pawn.getY(),true);
                    pawn.setState(false);
                    board.drawPiece(new Queen(pawn.getX(),pawn.getY(),true));
                }
            }
        }
    }

}
