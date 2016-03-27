package com.shenkar.android.ots.activities;
import android.os.Bundle;
import com.shenkar.android.ots.R;
import com.shenkar.android.ots.general.Toolbar;

public class AboutActivity extends Toolbar {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
    }
}
