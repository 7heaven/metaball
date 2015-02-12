package com.heaven.application.metaballdemo;

import android.app.Activity;
import android.os.Bundle;

import com.heaven.application.metaballdemo.R;

public class MainActivity extends Activity {

    private MetaballView metaballView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metaballView = (MetaballView) findViewById(R.id.metaball_view);
    }

}
