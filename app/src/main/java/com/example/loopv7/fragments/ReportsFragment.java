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
import com.example.loopv7.models.Request;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

import java.util.List;

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
            
            int currentUserId = currentUser.getId();
            Log.d("ReportsFragment", "Usuario actual: " + currentUser.getName() + " (ID: " + currentUserId + ")");
            
            // Verificar que el ID del usuario es válido
            if (currentUserId <= 0) {
                Log.e("ReportsFragment", "ID de usuario inválido: " + currentUserId);
                showDefaultStats();
                return;
            }
            
            // Obtener datos actualizados directamente de la base de datos usando el ID correcto
            User updatedUser = databaseHelper.getUserById(currentUserId);
            if (updatedUser == null) {
                Log.e("ReportsFragment", "No se pudo obtener usuario actualizado de la base de datos para ID: " + currentUserId);
                showDefaultStats();
                return;
            }
            
            Log.d("ReportsFragment", "Usuario actualizado obtenido: " + updatedUser.getName() + " (ID: " + updatedUser.getId() + ")");
            Log.d("ReportsFragment", "Verificando que el ID coincide: " + (updatedUser.getId() == currentUserId));
            
            // Actualizar estadísticas del usuario basándose en datos reales de la base de datos
            databaseHelper.updateUserStatsFromDatabase(currentUserId);
            
            // Obtener el usuario actualizado después de la sincronización
            updatedUser = databaseHelper.getUserById(currentUserId);
            if (updatedUser == null) {
                Log.e("ReportsFragment", "No se pudo obtener usuario después de actualizar estadísticas");
                showDefaultStats();
                return;
            }
            
            Log.d("ReportsFragment", "Servicios completados: " + updatedUser.getCompletedServices());
            Log.d("ReportsFragment", "Calificación: " + updatedUser.getRating());
            Log.d("ReportsFragment", "Total calificaciones: " + updatedUser.getTotalRatings());
            
            // Calcular ganancias reales de las solicitudes completadas y pagadas
            double totalEarnings = calculateRealEarnings(currentUserId);
            Log.d("ReportsFragment", "Ganancias reales calculadas: S/ " + String.format("%.2f", totalEarnings));
            
            // Mostrar estadísticas básicas del usuario actualizadas
            if (tvMonthlyEarnings != null) {
                tvMonthlyEarnings.setText(String.format("S/ %.2f", totalEarnings));
                Log.d("ReportsFragment", "Ganancias actualizadas: S/ " + String.format("%.2f", totalEarnings));
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
    
    /**
     * Calcula las ganancias reales de la socia sumando el precio de todas las solicitudes
     * completadas y pagadas, y también las aceptadas/en progreso (ganancias potenciales)
     * @param sociaId ID de la socia
     * @return Total de ganancias (completadas y pagadas + aceptadas/en progreso)
     */
    private double calculateRealEarnings(int sociaId) {
        try {
            // Obtener todas las solicitudes de la socia
            List<Request> sociaRequests = databaseHelper.getRequestsBySociaId(sociaId);
            
            double totalEarnings = 0.0;
            int paidCompletedCount = 0;
            int acceptedCount = 0;
            int inProgressCount = 0;
            
            Log.d("ReportsFragment", "Calculando ganancias para socia ID: " + sociaId);
            Log.d("ReportsFragment", "Total de solicitudes encontradas: " + sociaRequests.size());
            
            for (Request request : sociaRequests) {
                String status = request.getStatus();
                String paymentStatus = request.getPaymentStatus();
                double price = request.getTotalPrice();
                
                Log.d("ReportsFragment", "Solicitud ID: " + request.getId() + 
                      " - Estado: " + status + 
                      " - Pago: " + paymentStatus + 
                      " - Precio: S/ " + String.format("%.2f", price) +
                      " - Archivada: " + request.isArchived());
                
                // Contar solicitudes completadas y pagadas (ganancias confirmadas)
                if ("completada".equals(status) && "pagado".equals(paymentStatus)) {
                    totalEarnings += price;
                    paidCompletedCount++;
                    Log.d("ReportsFragment", "  ✓ Completada y pagada: +S/ " + String.format("%.2f", price));
                }
                // También contar solicitudes aceptadas, en progreso o completadas (ganancias potenciales/confirmadas)
                // Esto incluye trabajos que ya fueron aceptados pero aún no completados
                else if (("aceptada".equals(status) || "en_progreso".equals(status) || 
                         ("completada".equals(status) && !"pagado".equals(paymentStatus))) && 
                         !request.isArchived()) {
                    totalEarnings += price;
                    if ("aceptada".equals(status)) {
                        acceptedCount++;
                        Log.d("ReportsFragment", "  → Aceptada (pendiente): +S/ " + String.format("%.2f", price));
                    } else if ("en_progreso".equals(status)) {
                        inProgressCount++;
                        Log.d("ReportsFragment", "  → En progreso: +S/ " + String.format("%.2f", price));
                    } else {
                        Log.d("ReportsFragment", "  → Completada (no pagada aún): +S/ " + String.format("%.2f", price));
                    }
                }
            }
            
            Log.d("ReportsFragment", "Resumen de ganancias:");
            Log.d("ReportsFragment", "  - Completadas y pagadas: " + paidCompletedCount);
            Log.d("ReportsFragment", "  - Aceptadas: " + acceptedCount);
            Log.d("ReportsFragment", "  - En progreso: " + inProgressCount);
            Log.d("ReportsFragment", "  - Ganancias totales: S/ " + String.format("%.2f", totalEarnings));
            
            return totalEarnings;
            
        } catch (Exception e) {
            Log.e("ReportsFragment", "Error calculando ganancias reales: " + e.getMessage(), e);
            return 0.0;
        }
    }
    
    private void showDefaultStats() {
        if (tvMonthlyEarnings != null) {
            tvMonthlyEarnings.setText("S/ 0.00");
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
