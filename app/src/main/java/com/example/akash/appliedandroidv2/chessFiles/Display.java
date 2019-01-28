package com.example.akash.appliedandroidv2.chessFiles;

import com.example.akash.appliedandroidv2.chessFiles.ChessBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Akashdeep Singh
 * 7802937
 * Display.java
 */
public class Display implements GameDisplay {
    //ChessBoard board;

    public Display(ChessBoard board){
       // this.board = board;
    }

    public int promptForOpponentDifficulty(int maxDifficulty){
        System.out.printf("Level of Difficulty: (Ranges from %d to %d )",1,maxDifficulty);
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        String input=null;
        try {
            input = buff.readLine();
        }catch(IOException io){
            io.printStackTrace();
        }
        int level = Integer.parseInt(input);
        return level;
    }

    public void displayBoard(ChessBoard board){
        board.printBoard();
    }

    public void summarizeMove(Move m){
        System.out.println(m.toString());
    }

    public void gameOver(int winner){
        if(winner==1){
            System.out.println("You Won The Game");
        }//else if(winner==2){
           // System.out.println("Hard diff bot won");
        else{
            System.out.println("The AI Won The Game");
        }
        System.exit(0);
    }

    public String promptForInput(){
        System.out.println("Enter 0 to exit | Enter 1 to reset the game| Enter the Move \"SourceX,SourceY,DestinationX,Destination Y\"");
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        String input=null;
        try {
            input = buff.readLine();
        }catch(IOException io){
            io.printStackTrace();
        }
        //int level = Integer.parseInt(input);
        return input;
    }
    public String promptForPawnPromotion(){
        System.out.println("Pawn Promotion: What Piece would you like to switch it to?");
        System.out.println("| Rook | Knight | Bishop | Queen | Input the word! ");
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        String input=null;
        try {
            input = buff.readLine();
        }catch(IOException io){
            io.printStackTrace();
        }
        return input;
    }
}
