package com.example.akash.appliedandroidv2;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Akash on 12/26/2017.
 */

public class GameLoop extends Activity {
    private GameLoop_Class gameLoop;
    private String ch = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameLoop = new GameLoop_Class(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(gameLoop);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameLoop.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameLoop.resume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();

        if(e==event.ACTION_DOWN){

            //Toast myToast = Toast.makeText(getApplicationContext(),"Pressed", Toast.LENGTH_SHORT);
            //myToast.show();
        }
        //if(e==event.ACTION_MOVE){
          //  Toast mToast = Toast.makeText(getApplicationContext(),"Moved", Toast.LENGTH_SHORT);
            //mToast.show();
        //}

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("log_tag", "char : " + event.getUnicodeChar());

        if (event.getUnicodeChar() != 0) {
            int i = event.getUnicodeChar();
            ch += new Character((char) i).toString();
        }
        if (keyCode == KeyEvent.KEYCODE_DEL && ch.length() > 0) {
            ch = ch.substring(0, ch.length() - 1);
        }
        Toast myToast = Toast.makeText(getApplicationContext(),"String is: "+ch, Toast.LENGTH_SHORT);
        myToast.show();
        //value.setText(ch);
        //drawText(ch);
        gameLoop.setInput(ch);
        return super.onKeyDown(keyCode, event);
    }

    public String getTypedString(){
        return this.ch;
    }

    public void resetString(){
        ch = "";
    }
}
