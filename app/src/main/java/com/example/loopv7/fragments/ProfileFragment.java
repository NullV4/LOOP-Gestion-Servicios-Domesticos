package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loopv7.R;
import com.example.loopv7.activities.EditProfileActivity;
import com.example.loopv7.auth.LoginActivity;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvRole, tvLocation, tvDescription, tvRating;
    private LinearLayout layoutRating;
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
        tvLocation = view.findViewById(R.id.tvLocation);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvRating = view.findViewById(R.id.tvRating);
        layoutRating = view.findViewById(R.id.layoutRating);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        
        setupUserInfo();
        setupListeners();
        
        return view;
    }
    
    private void setupUserInfo() {
        loadUserData();
    }
    
    private void loadUserData() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser != null) {
            tvName.setText(currentUser.getName());
            tvEmail.setText(currentUser.getEmail());
            tvPhone.setText(currentUser.getPhone());
            tvRole.setText(currentUser.isCliente() ? "Cliente" : "Socia Trabajadora");
            
            // Mostrar información enriquecida
            tvLocation.setText(currentUser.getLocationOrDefault());
            tvDescription.setText(currentUser.getDescriptionOrDefault());
            
            // Mostrar calificación solo para socias
            if (currentUser.isSocia()) {
                layoutRating.setVisibility(View.VISIBLE);
                tvRating.setText(currentUser.getFormattedRating());
            } else {
                layoutRating.setVisibility(View.GONE);
            }
        }
    }
    
    private void setupListeners() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivityForResult(intent, 100);
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
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {
            // El perfil fue actualizado, recargar datos
            loadUserData();
            Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
        }
    }
}
