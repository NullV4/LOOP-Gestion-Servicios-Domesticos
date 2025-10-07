package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loopv7.R;
import com.example.loopv7.auth.LoginActivity;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvRole;
    private Button btnEditProfile, btnLogout;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        sessionManager = new SessionManager(getContext());
        
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvRole = view.findViewById(R.id.tvRole);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        
        setupUserInfo();
        setupListeners();
        
        return view;
    }
    
    private void setupUserInfo() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser != null) {
            tvName.setText(currentUser.getName());
            tvEmail.setText(currentUser.getEmail());
            tvPhone.setText(currentUser.getPhone());
            tvRole.setText(currentUser.isCliente() ? "Cliente" : "Socia Trabajadora");
        }
    }
    
    private void setupListeners() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implementar edición de perfil
                Toast.makeText(getContext(), "Editar perfil - Próximamente", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
