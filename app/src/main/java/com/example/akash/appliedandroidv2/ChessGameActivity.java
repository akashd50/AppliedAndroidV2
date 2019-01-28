package com.example.akash.appliedandroidv2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

public class ChessGameActivity extends Activity {
    private String ch = "";
    private ChessGameClass chessGameClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chessGameClass = new ChessGameClass(this);
        setContentView(chessGameClass);
    }
    @Override
    protected void onPause() {
        super.onPause();
        chessGameClass.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chessGameClass.resume();
    }

}
