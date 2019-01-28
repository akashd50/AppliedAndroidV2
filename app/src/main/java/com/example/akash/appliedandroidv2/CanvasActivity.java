package com.example.akash.appliedandroidv2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by Akash on 12/17/2017.
 */

public class CanvasActivity extends Activity {
    SurfaveViewTextApp textApp;
    private String ch = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textApp = new SurfaveViewTextApp(this);
        setContentView(textApp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        textApp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textApp.resume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("log_tag", "char : " + event.getUnicodeChar());

        if (event.getUnicodeChar() != 0) {
            int i = event.getUnicodeChar();
            ch += new Character((char) i).toString();
            //Toast myToast = Toast.makeText(getApplicationContext(),"KeyStroke: "+keyCode, Toast.LENGTH_SHORT);
            //myToast.show();
        }
        if (keyCode == KeyEvent.KEYCODE_DEL && ch.length() > 0) {
            ch = ch.substring(0, ch.length() - 1);
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            Toast myToast = Toast.makeText(getApplicationContext(),"Level Selected: "+ch, Toast.LENGTH_SHORT);
            myToast.show();
        }
        //Toast myToast = Toast.makeText(getApplicationContext(),"String is: "+ch, Toast.LENGTH_SHORT);
        //myToast.show();
        //value.setText(ch);
        //drawText(ch);
        textApp.setInput(ch);
        return super.onKeyDown(keyCode, event);
    }

    public String getTypedString(){
        return this.ch;
    }

    public void resetString(){
        ch = "";
    }
}
