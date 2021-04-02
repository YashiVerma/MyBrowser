package com.example.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread th = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    sleep(5000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent home = new Intent(WelcomeActivity.this, MainActivity.class) ;
                    startActivity(home);
                }
            }
        };
        th.start () ;

    }

    @Override
    protected void onPause () {
        super.onPause () ;
        finish () ;
    }


}