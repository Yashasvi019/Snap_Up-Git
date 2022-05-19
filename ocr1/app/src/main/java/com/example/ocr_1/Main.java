package com.example.ocr_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    private static int timer = 5000;
    ImageView imageView;
    TextView textView1,textView2;
    Animation upperAnimation,lowerAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.mainText);
        textView2 = findViewById(R.id.registerText);
        upperAnimation = AnimationUtils.loadAnimation(this,R.anim.upper_animation);
        lowerAnimation = AnimationUtils.loadAnimation(this,R.anim.lower_animation);

        imageView.setAnimation(upperAnimation);
        textView1.setAnimation(lowerAnimation);
        textView2.setAnimation(lowerAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Main.this, LoginEmail.class);
                startActivity(intent);
                finish();
            }
        },timer);
    }
}