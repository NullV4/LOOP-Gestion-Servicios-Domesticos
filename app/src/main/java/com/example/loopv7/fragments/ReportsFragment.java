package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loopv7.R;
import com.example.loopv7.activities.ReportsActivity;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

/**
 * Fragment para mostrar resumen de reportes y acceso a reportes detallados
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class ReportsFragment extends Fragment {

    private TextView tvTitle, tvQuickStats;
    private Button btnViewDetailedReports;
    private SessionManager sessionManager;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        
        sessionManager = new SessionManager(getContext());
        currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null || !currentUser.isSocia()) {
            // Si no es socia, mostrar mensaje de acceso denegado
            return inflater.inflate(R.layout.fragment_access_denied, container, false);
        }
        
        initializeViews(view);
        setupListeners();
        loadQuickStats();
        
        return view;
    }
    
    private void initializeViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvQuickStats = view.findViewById(R.id.tvQuickStats);
        btnViewDetailedReports = view.findViewById(R.id.btnViewDetailedReports);
        
        tvTitle.setText("📊 Reportes de " + currentUser.getName());
    }
    
    private void setupListeners() {
        btnViewDetailedReports.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ReportsActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadQuickStats() {
        // Mostrar estadísticas rápidas del mes actual
        tvQuickStats.setText("📈 Ve a Reportes Detallados para ver:\n\n" +
                           "• Ganancias del mes\n" +
                           "• Servicios completados\n" +
                           "• Calificaciones promedio\n" +
                           "• Tendencias de crecimiento\n" +
                           "• Análisis de clientes\n" +
                           "• Métricas de rendimiento");
    }
}
