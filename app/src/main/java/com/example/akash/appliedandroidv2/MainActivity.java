package com.example.akash.appliedandroidv2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.R;
//import android.R;
//com.android.support:appcompat-v7:22.0.+
//compile 'com.android.support:appcompat-v7:25.3.1'
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
//import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Button button1;
    Button canvasView;
    Button gameLoop;
    Button glRendered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.akash.appliedandroidv2.R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar myToolbar = (Toolbar)findViewById(com.example.akash.appliedandroidv2.R.id.toolbar2);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(com.example.akash.appliedandroidv2.R.drawable.abm2);
        button1 = (Button)findViewById(com.example.akash.appliedandroidv2.R.id.button1);
        canvasView = (Button)findViewById(com.example.akash.appliedandroidv2.R.id.canvasViewbutton);
        gameLoop = (Button)findViewById(com.example.akash.appliedandroidv2.R.id.gameLoopButton);
        glRendered = (Button)findViewById(com.example.akash.appliedandroidv2.R.id.GLRenderer);
    }

    public void onButtonClick(View view){
        //sec = new SecondActivity();
        Intent secScreen = new Intent(this, SecondActivity.class);
        startActivity(secScreen);

    }

    public void canvasViewClick(View view){
        Intent canvasScreen = new Intent(this, CanvasActivity.class);
        startActivity(canvasScreen);
    }

    public void glRendererCLick(View view){
        Intent glRenderedCall = new Intent(this, GLRendererActivity.class);
        startActivity(glRenderedCall);
    }

    public void gameLoopClick(View view){
        Intent gameLoopScreen = new Intent(this, GameLoop.class);
        startActivity(gameLoopScreen);
    }
}
