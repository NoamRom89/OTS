package com.example.android.ots;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by dudilugasi on 16/01/2016.
 */
public class OTS extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }

}
