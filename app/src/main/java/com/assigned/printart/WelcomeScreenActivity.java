package com.assigned.printart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeactivityIntent = new Intent(WelcomeScreenActivity.this, HomeActivity.class);
                startActivity(homeactivityIntent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
