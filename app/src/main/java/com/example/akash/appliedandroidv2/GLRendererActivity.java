package com.example.akash.appliedandroidv2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class GLRendererActivity extends Activity {
    private GLRendererView glRendererView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glRendererView = new GLRendererView(this);
        setContentView(glRendererView);
    }

}
