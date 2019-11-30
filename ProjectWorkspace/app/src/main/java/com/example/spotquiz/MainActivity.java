package com.example.spotquiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Splash screen timer
//    private static int SPLASH_TIME_OUT = 1500;

    Thread objectthread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startSplash();
    }

    public void startSplash()
    {
        try{
            Animation objanimation = AnimationUtils.loadAnimation(this,R.anim.translate);
            objanimation.reset();
            ImageView objImageView = findViewById(R.id.icon);
            objImageView.clearAnimation();
            objImageView.startAnimation(objanimation);

            objectthread = new Thread() {
                @Override
                public void run() {
                    int pauseit = 0;
                    while (pauseit< 3500) {
                        try{
                        sleep(100);} catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pauseit +=100;

                    }
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    MainActivity.this.finish();

                }
            };
            objectthread.start();

        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }






//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//
//        setContentView(R.layout.activity_main);
//
//        Animation objanimation = AnimationUtils.loadAnimation(this,R.anim.translate);
//        objanimation.reset();
//        ImageView objImageView = findViewById(R.id.icon);
//        objImageView.clearAnimation();
//        objImageView.startAnimation(objanimation);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            Intent i = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(i);
//        //close
//
//                finish();
//            }
//        },SPLASH_TIME_OUT);


    }
