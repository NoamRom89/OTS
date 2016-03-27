package com.example.android.ots.activities;
import android.os.Bundle;
import com.example.android.ots.R;
import com.example.android.ots.general.Toolbar;

public class AboutActivity extends Toolbar {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
