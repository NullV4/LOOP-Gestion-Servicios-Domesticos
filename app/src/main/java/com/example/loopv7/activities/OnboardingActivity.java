package com.example.loopv7.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.loopv7.R;
import com.example.loopv7.fragments.OnboardingFragment;
import com.example.loopv7.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private Button btnNext, btnSkip;
    private OnboardingPagerAdapter adapter;
    private int currentPage = 0;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        
        // Ocultar la barra de estado para una experiencia inmersiva
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        
        preferences = getSharedPreferences("onboarding_prefs", MODE_PRIVATE);
        
        initViews();
        setupViewPager();
        setupDots();
        setupListeners();
    }
    
    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
    }
    
    private void setupViewPager() {
        adapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        
        // Agregar fragments de onboarding
        adapter.addFragment(OnboardingFragment.newInstance(
            "¡Bienvenido a LOOP!",
            "La plataforma que conecta clientes con trabajadoras domésticas de confianza para servicios de calidad.",
            R.drawable.ic_welcome,
            "#2E7D32"
        ));
        
        adapter.addFragment(OnboardingFragment.newInstance(
            "Encuentra Servicios",
            "Explora una amplia gama de servicios domésticos: limpieza, cocina, cuidado de niños y más.",
            R.drawable.ic_services,
            "#4CAF50"
        ));
        
        adapter.addFragment(OnboardingFragment.newInstance(
            "Trabajadoras Verificadas",
            "Todas nuestras trabajadoras están verificadas y calificadas por la comunidad.",
            R.drawable.ic_verified,
            "#FF9800"
        ));
        
        adapter.addFragment(OnboardingFragment.newInstance(
            "¡Comienza Ahora!",
            "Crea tu cuenta y comienza a disfrutar de servicios domésticos de calidad.",
            R.drawable.ic_start,
            "#2196F3"
        ));
        
        viewPager.setAdapter(adapter);
        
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                updateDots(position);
                updateButtons(position);
            }
            
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
    
    private void setupDots() {
        for (int i = 0; i < adapter.getCount(); i++) {
            View dot = new View(this);
            dot.setBackgroundResource(R.drawable.dot_unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density)
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsLayout.addView(dot);
        }
        updateDots(0);
    }
    
    private void updateDots(int position) {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            View dot = dotsLayout.getChildAt(i);
            if (i == position) {
                dot.setBackgroundResource(R.drawable.dot_selected);
            } else {
                dot.setBackgroundResource(R.drawable.dot_unselected);
            }
        }
    }
    
    private void updateButtons(int position) {
        if (position == adapter.getCount() - 1) {
            btnNext.setText("Comenzar");
            btnSkip.setVisibility(View.GONE);
        } else {
            btnNext.setText("Siguiente");
            btnSkip.setVisibility(View.VISIBLE);
        }
    }
    
    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (currentPage == adapter.getCount() - 1) {
                finishOnboarding();
            } else {
                viewPager.setCurrentItem(currentPage + 1, true);
            }
        });
        
        btnSkip.setOnClickListener(v -> finishOnboarding());
    }
    
    private void finishOnboarding() {
        // Marcar que el onboarding ya se completó
        preferences.edit().putBoolean("onboarding_completed", true).apply();
        
        // Ir a la pantalla de login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    private static class OnboardingPagerAdapter extends FragmentPagerAdapter {
        private List<OnboardingFragment> fragments = new ArrayList<>();
        
        public OnboardingPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        
        public void addFragment(OnboardingFragment fragment) {
            fragments.add(fragment);
        }
        
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        
        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}

