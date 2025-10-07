package com.example.loopv7;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.loopv7.auth.LoginActivity;
import com.example.loopv7.fragments.HomeFragment;
import com.example.loopv7.fragments.PendingRequestsFragment;
import com.example.loopv7.fragments.ProfileFragment;
import com.example.loopv7.fragments.RequestsFragment;
import com.example.loopv7.fragments.ServicesFragment;
import com.example.loopv7.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        // Inicializar SessionManager
        sessionManager = new SessionManager(this);
        
        // Verificar si el usuario está logueado
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }
        
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        setupNavigation(savedInstanceState);
    }
    
    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void setupNavigation(Bundle savedInstanceState) {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        
        // Configurar navegación según el rol
        if (sessionManager.isCliente()) {
            setupClientNavigation();
        } else if (sessionManager.isSocia()) {
            setupSociaNavigation();
        }
        
        // Mostrar fragment inicial
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }
    }
    
    private void setupClientNavigation() {
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_client);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_services) {
                selectedFragment = new ServicesFragment();
            } else if (itemId == R.id.nav_requests) {
                selectedFragment = new RequestsFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
    
    private void setupSociaNavigation() {
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_socia);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_pending) {
                selectedFragment = new PendingRequestsFragment();
            } else if (itemId == R.id.nav_requests) {
                selectedFragment = new RequestsFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}