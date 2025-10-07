package com.example.loopv7.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loopv7.R;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class HomeFragment extends Fragment {

    private TextView tvWelcome, tvUserInfo;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        sessionManager = new SessionManager(getContext());
        
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        
        setupUserInfo();
        
        return view;
    }
    
    private void setupUserInfo() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser != null) {
            tvWelcome.setText("¡Bienvenido, " + currentUser.getName() + "!");
            
            String roleText = currentUser.isCliente() ? "Cliente" : "Socia Trabajadora";
            tvUserInfo.setText("Rol: " + roleText + "\nEmail: " + currentUser.getEmail());
        }
    }
}
