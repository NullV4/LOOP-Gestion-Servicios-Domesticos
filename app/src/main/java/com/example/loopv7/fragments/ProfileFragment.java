package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.loopv7.activities.RatingsActivity;
import com.example.loopv7.auth.LoginActivity;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvRole, tvLocation, tvDescription, tvRating, tvTotalRatings, tvCompletedServices;
    private LinearLayout layoutRating;
    private com.google.android.material.card.MaterialCardView layoutSociaStats;
    private Button btnEditProfile, btnViewRatings, btnLogout;
    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            
            // Verificar que el contexto existe
            if (getContext() == null) {
                return view;
            }
            
            sessionManager = new SessionManager(getContext());
            databaseHelper = new DatabaseHelper(getContext());
            
            Log.d("ProfileFragment", "SessionManager y DatabaseHelper inicializados");
            
            // Verificar que el usuario está logueado
            if (!sessionManager.isLoggedIn()) {
                Log.e("ProfileFragment", "Usuario no está logueado");
                Toast.makeText(getContext(), "Sesión expirada. Por favor, inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
                return view;
            }
            
            Log.d("ProfileFragment", "Usuario está logueado correctamente");
            
            tvName = view.findViewById(R.id.tvName);
            tvEmail = view.findViewById(R.id.tvEmail);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvRole = view.findViewById(R.id.tvRole);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvRating = view.findViewById(R.id.tvRating);
            tvTotalRatings = view.findViewById(R.id.tvTotalRatings);
            tvCompletedServices = view.findViewById(R.id.tvCompletedServices);
            layoutRating = view.findViewById(R.id.layoutRating);
            layoutSociaStats = view.findViewById(R.id.layoutSociaStats);
            btnEditProfile = view.findViewById(R.id.btnEditProfile);
            btnViewRatings = view.findViewById(R.id.btnViewRatings);
            btnLogout = view.findViewById(R.id.btnLogout);
            
            // Verificar que todos los elementos críticos existen
            if (tvName == null || tvEmail == null || tvPhone == null || tvRole == null) {
                Toast.makeText(getContext(), "Error al cargar la interfaz del perfil", Toast.LENGTH_SHORT).show();
                return view;
            }
            
            setupUserInfo();
            setupListeners();
            
            return view;
        } catch (Exception e) {
            // Si hay algún error, mostrar mensaje y devolver vista básica
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error al cargar el perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ProfileFragment", "Error en onCreateView: " + e.getMessage(), e);
            }
            return inflater.inflate(R.layout.fragment_profile, container, false);
        }
    }
    
    private void setupUserInfo() {
        loadUserData();
    }
    
    private void loadUserData() {
        try {
            // Verificar que el contexto y sessionManager existen
            if (getContext() == null || sessionManager == null || databaseHelper == null) {
                Log.e("ProfileFragment", "Context, sessionManager o databaseHelper es null");
                return;
            }
            
            // Log del estado de la base de datos
            databaseHelper.logDatabaseStatus();
            
            // Obtener datos básicos del SessionManager
            User sessionUser = sessionManager.getCurrentUser();
            if (sessionUser == null) {
                Log.e("ProfileFragment", "No se pudo obtener el usuario de la sesión");
                if (getContext() != null) {
                    Toast.makeText(getContext(), "No se pudo cargar la información del usuario", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            
            Log.d("ProfileFragment", "Usuario de sesión: " + sessionUser.getName() + " (ID: " + sessionUser.getId() + ")");
            
            // Obtener datos completos de la base de datos
            User currentUser = databaseHelper.getUserById(sessionUser.getId());
            if (currentUser == null) {
                // Si no se encuentra en la base de datos, usar datos de la sesión
                currentUser = sessionUser;
            }
            
            // Información básica con verificaciones de null
            if (tvName != null) {
                tvName.setText(currentUser.getName() != null ? currentUser.getName() : "Sin nombre");
            }
            if (tvEmail != null) {
                tvEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "Sin email");
            }
            if (tvPhone != null) {
                tvPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "Sin teléfono");
            }
            if (tvRole != null) {
                tvRole.setText(currentUser.isCliente() ? "Cliente" : "Socia Trabajadora");
            }
            
            // Información opcional
            if (tvLocation != null) {
                tvLocation.setText(currentUser.getLocationOrDefault());
            }
            if (tvDescription != null) {
                tvDescription.setText(currentUser.getDescriptionOrDefault());
            }
            
            // Mostrar información de socia
            if (currentUser.isSocia()) {
                if (layoutRating != null) {
                    layoutRating.setVisibility(View.VISIBLE);
                }
                if (layoutSociaStats != null) {
                    layoutSociaStats.setVisibility(View.VISIBLE);
                }
                if (btnViewRatings != null) {
                    btnViewRatings.setVisibility(View.VISIBLE);
                }
                
                // Mostrar calificación promedio
                if (tvRating != null) {
                    tvRating.setText(String.format("%.1f ⭐", currentUser.getRating()));
                }
                
                // Mostrar total de calificaciones
                if (tvTotalRatings != null) {
                    tvTotalRatings.setText(currentUser.getTotalRatings() + " calificaciones");
                }
                
                // Mostrar servicios completados
                if (tvCompletedServices != null) {
                    tvCompletedServices.setText(currentUser.getCompletedServices() + " servicios completados");
                }
            } else {
                if (layoutRating != null) {
                    layoutRating.setVisibility(View.GONE);
                }
                if (layoutSociaStats != null) {
                    layoutSociaStats.setVisibility(View.GONE);
                }
                if (btnViewRatings != null) {
                    btnViewRatings.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error al cargar datos del perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void setupListeners() {
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(getContext(), EditProfileActivity.class);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al abrir edición de perfil", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        
        if (btnViewRatings != null) {
            btnViewRatings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        User currentUser = sessionManager.getCurrentUser();
                        if (currentUser != null && currentUser.isSocia()) {
                            Intent intent = new Intent(getContext(), RatingsActivity.class);
                            intent.putExtra("socia_id", currentUser.getId());
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al abrir calificaciones", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        
        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sessionManager.logoutUser();
                        Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
                        
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
