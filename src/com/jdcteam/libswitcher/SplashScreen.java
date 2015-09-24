package com.jdcteam.libswitcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by AntaresOne on 23/09/2015.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Thread showSplash = new Thread() {
            public void run() {
                try {
                    sleep(1000, 1000);
                    finish();
                    Intent showOptions = new Intent(SplashScreen.this, Options.class);
                    startActivity(showOptions);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        showSplash.start();
    }
}