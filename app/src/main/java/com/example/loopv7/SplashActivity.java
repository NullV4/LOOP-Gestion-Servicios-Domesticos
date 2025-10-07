package com.example.loopv7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.auth.LoginActivity;
import com.example.loopv7.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private TextView tvAppName, tvTagline;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        sessionManager = new SessionManager(this);
        
        initializeViews();
        startAnimations();
    }
    
    private void initializeViews() {
        ivLogo = findViewById(R.id.ivLogo);
        tvAppName = findViewById(R.id.tvAppName);
        tvTagline = findViewById(R.id.tvTagline);
    }
    
    private void startAnimations() {
        // Animación de entrada del logo
        ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0f, 1f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0f, 1f);
        ObjectAnimator logoAlpha = ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f);
        
        logoScaleX.setDuration(800);
        logoScaleY.setDuration(800);
        logoAlpha.setDuration(800);
        
        logoScaleX.start();
        logoScaleY.start();
        logoAlpha.start();
        
        // Animación del nombre de la app
        ObjectAnimator nameAlpha = ObjectAnimator.ofFloat(tvAppName, "alpha", 0f, 1f);
        ObjectAnimator nameTranslationY = ObjectAnimator.ofFloat(tvAppName, "translationY", 50f, 0f);
        
        nameAlpha.setDuration(600);
        nameTranslationY.setDuration(600);
        nameAlpha.setStartDelay(400);
        nameTranslationY.setStartDelay(400);
        
        nameAlpha.start();
        nameTranslationY.start();
        
        // Animación del tagline
        ObjectAnimator taglineAlpha = ObjectAnimator.ofFloat(tvTagline, "alpha", 0f, 1f);
        ObjectAnimator taglineTranslationY = ObjectAnimator.ofFloat(tvTagline, "translationY", 30f, 0f);
        
        taglineAlpha.setDuration(500);
        taglineTranslationY.setDuration(500);
        taglineAlpha.setStartDelay(800);
        taglineTranslationY.setStartDelay(800);
        
        taglineAlpha.start();
        taglineTranslationY.start();
        
        // Navegar después de las animaciones
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            navigateToNextActivity();
        }, 2000);
    }
    
    private void navigateToNextActivity() {
        Intent intent;
        
        if (sessionManager.isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        
        startActivity(intent);
        finish();
    }
}
