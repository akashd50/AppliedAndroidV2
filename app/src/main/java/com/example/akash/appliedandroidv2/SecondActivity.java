package com.example.akash.appliedandroidv2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Akash on 11/26/2017.
 */

public class SecondActivity extends Activity {
    SecondActivity_ViewClass viewClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewClass = new SecondActivity_ViewClass(this);
        setContentView(viewClass);
    }
}
