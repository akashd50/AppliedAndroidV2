/**
 * Name: Akashdeep Singh
 * Student no: 7802937
 * GameDisplay.java
 * Interface for Display
 */

package com.example.akash.appliedandroidv2.chessFiles;


public interface GameDisplay {
        public int promptForOpponentDifficulty(int maxDifficulty);
        public void displayBoard(ChessBoard board);
        public void summarizeMove(Move m);
        public void gameOver(int winner);
}
