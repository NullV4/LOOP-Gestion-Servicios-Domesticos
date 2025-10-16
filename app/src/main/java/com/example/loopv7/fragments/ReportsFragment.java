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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loopv7.R;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

/**
 * Fragment para mostrar resumen de reportes y acceso a reportes detallados
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class ReportsFragment extends Fragment {

    private TextView tvTitle, tvQuickStats, tvMonthlyEarnings, tvCompletedServices, tvAverageRating, tvTotalRatings;
    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        
        sessionManager = new SessionManager(getContext());
        databaseHelper = new DatabaseHelper(getContext());
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
        tvMonthlyEarnings = view.findViewById(R.id.tvMonthlyEarnings);
        tvCompletedServices = view.findViewById(R.id.tvCompletedServices);
        tvAverageRating = view.findViewById(R.id.tvAverageRating);
        tvTotalRatings = view.findViewById(R.id.tvTotalRatings);
        
        // Verificar que todos los elementos críticos existen
        if (tvTitle == null || tvQuickStats == null) {
            Toast.makeText(getContext(), "Error al cargar la interfaz de reportes", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (currentUser != null) {
            tvTitle.setText("📊 Mis Estadísticas");
        } else {
            tvTitle.setText("📊 Estadísticas");
        }
    }
    
    private void setupListeners() {
        // No hay botones en la versión simplificada
    }
    
    private void loadQuickStats() {
        try {
            Log.d("ReportsFragment", "Iniciando carga de estadísticas rápidas");
            
            if (currentUser == null) {
                Log.e("ReportsFragment", "currentUser es null");
                showDefaultStats();
                return;
            }
            
            Log.d("ReportsFragment", "Usuario actual: " + currentUser.getName() + " (ID: " + currentUser.getId() + ")");
            
            // Obtener datos actualizados directamente de la base de datos
            User updatedUser = databaseHelper.getUserById(currentUser.getId());
            if (updatedUser == null) {
                Log.e("ReportsFragment", "No se pudo obtener usuario actualizado de la base de datos");
                showDefaultStats();
                return;
            }
            
            Log.d("ReportsFragment", "Usuario actualizado obtenido: " + updatedUser.getName());
            
            // Actualizar estadísticas del usuario basándose en datos reales de la base de datos
            databaseHelper.updateUserStatsFromDatabase(currentUser.getId());
            
            // Obtener el usuario actualizado después de la sincronización
            updatedUser = databaseHelper.getUserById(currentUser.getId());
            if (updatedUser == null) {
                Log.e("ReportsFragment", "No se pudo obtener usuario después de actualizar estadísticas");
                showDefaultStats();
                return;
            }
            
            Log.d("ReportsFragment", "Servicios completados: " + updatedUser.getCompletedServices());
            Log.d("ReportsFragment", "Calificación: " + updatedUser.getRating());
            Log.d("ReportsFragment", "Total calificaciones: " + updatedUser.getTotalRatings());
            
            // Mostrar estadísticas básicas del usuario actualizadas
            if (tvMonthlyEarnings != null) {
                // Calcular ganancias estimadas basadas en servicios completados reales
                double estimatedEarnings = updatedUser.getCompletedServices() * 25.0; // $25 por servicio promedio
                tvMonthlyEarnings.setText(String.format("$%.2f", estimatedEarnings));
                Log.d("ReportsFragment", "Ganancias actualizadas: $" + String.format("%.2f", estimatedEarnings));
            } else {
                Log.e("ReportsFragment", "tvMonthlyEarnings es null");
            }
            
            if (tvCompletedServices != null) {
                tvCompletedServices.setText(String.valueOf(updatedUser.getCompletedServices()));
                Log.d("ReportsFragment", "Servicios completados actualizados: " + updatedUser.getCompletedServices());
            } else {
                Log.e("ReportsFragment", "tvCompletedServices es null");
            }
            
            if (tvAverageRating != null) {
                tvAverageRating.setText(String.format("%.1f ⭐", updatedUser.getRating()));
                Log.d("ReportsFragment", "Calificación actualizada: " + String.format("%.1f", updatedUser.getRating()));
            } else {
                Log.e("ReportsFragment", "tvAverageRating es null");
            }
            
            if (tvTotalRatings != null) {
                tvTotalRatings.setText(String.valueOf(updatedUser.getTotalRatings()));
                Log.d("ReportsFragment", "Total calificaciones actualizado: " + updatedUser.getTotalRatings());
            } else {
                Log.e("ReportsFragment", "tvTotalRatings es null");
            }
            
            // Mostrar resumen simple con datos actualizados
            StringBuilder summaryText = new StringBuilder();
            summaryText.append("📊 Resumen de tu Trabajo:\n\n");
            summaryText.append("✅ Servicios Completados: ").append(updatedUser.getCompletedServices()).append("\n");
            summaryText.append("⭐ Calificación Promedio: ").append(String.format("%.1f", updatedUser.getRating())).append("/5.0\n");
            summaryText.append("📝 Total de Calificaciones: ").append(updatedUser.getTotalRatings()).append("\n");
            
            if (updatedUser.getLastServiceDate() != null && !updatedUser.getLastServiceDate().isEmpty()) {
                summaryText.append("📅 Último Servicio: ").append(updatedUser.getLastServiceDate()).append("\n");
            }
            
            summaryText.append("\n💡 Consejo: Mantén tu calificación alta para atraer más clientes!");
            
            if (tvQuickStats != null) {
                tvQuickStats.setText(summaryText.toString());
                Log.d("ReportsFragment", "Resumen de estadísticas actualizado");
            } else {
                Log.e("ReportsFragment", "tvQuickStats es null");
            }
            
            Log.d("ReportsFragment", "Carga de estadísticas completada exitosamente");
            
        } catch (Exception e) {
            Log.e("ReportsFragment", "Error al cargar estadísticas: " + e.getMessage(), e);
            showDefaultStats();
            Toast.makeText(getContext(), "Error al cargar estadísticas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showDefaultStats() {
        if (tvMonthlyEarnings != null) {
            tvMonthlyEarnings.setText("$0.00");
        }
        if (tvCompletedServices != null) {
            tvCompletedServices.setText("0");
        }
        if (tvAverageRating != null) {
            tvAverageRating.setText("0.0 ⭐");
        }
        if (tvTotalRatings != null) {
            tvTotalRatings.setText("0");
        }
        
        if (tvQuickStats != null) {
            tvQuickStats.setText("📊 No hay datos suficientes para mostrar estadísticas.\n\n" +
                               "Completa algunos servicios para ver tus métricas aquí.");
        }
    }
}
