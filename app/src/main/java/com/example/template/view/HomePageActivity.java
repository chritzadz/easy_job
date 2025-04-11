package com.example.template.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.template.R;
import com.google.firebase.FirebaseApp;

/**
 * @file HomePageActivity.java
 * @author: -
 * @description: Initial activity to choose wither login or to signup
 */

public class HomePageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ImageView logoImageView = findViewById(R.id.logoImageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }


        logoImageView.postDelayed(() -> {
            ScaleAnimation zoomAnimation = new ScaleAnimation(
                    1f, 250f,
                    1f, 250f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            zoomAnimation.setDuration(2000);
            zoomAnimation.setFillAfter(true);
            logoImageView.startAnimation(zoomAnimation);

            zoomAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent intent = new Intent(HomePageActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }, 3000);
    }
}
