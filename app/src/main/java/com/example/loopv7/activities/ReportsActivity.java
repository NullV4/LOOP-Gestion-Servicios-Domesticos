package com.example.loopv7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.models.SociaReport;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.ErrorHandler;
import com.example.loopv7.utils.ReportGenerator;
import com.example.loopv7.utils.SessionManager;

/**
 * Actividad para mostrar reportes detallados de socias
 * 
 * Funcionalidades:
 * - Mostrar reportes por período (diario, semanal, mensual, anual)
 * - Estadísticas de ganancias y rendimiento
 * - Gráficos y métricas visuales
 * - Exportar reportes
 * - Comparar períodos
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class ReportsActivity extends AppCompatActivity {

    private TextView tvTitle, tvPeriod, tvTotalEarnings, tvCompletedRequests, tvAverageRating;
    private TextView tvCompletionRate, tvCustomerSatisfaction, tvEfficiencyScore;
    private TextView tvEarningsGrowth, tvRatingTrend, tvRequestsGrowth;
    private TextView tvMostRequestedService, tvUniqueClients, tvReturningClients;
    
    private Button btnDaily, btnWeekly, btnMonthly, btnYearly, btnExport;
    private LinearLayout layoutStats, layoutTrends, layoutServices, layoutClients;
    
    private ReportGenerator reportGenerator;
    private SessionManager sessionManager;
    private ErrorHandler errorHandler;
    private User currentUser;
    private SociaReport currentReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        
        // Inicializar componentes
        reportGenerator = new ReportGenerator(this);
        sessionManager = new SessionManager(this);
        errorHandler = ErrorHandler.getInstance(this);
        
        // Verificar que el usuario es una socia
        currentUser = sessionManager.getCurrentUser();
        if (currentUser == null || !currentUser.isSocia()) {
            errorHandler.handleAuthenticationError("Acceso denegado: Solo para socias");
            finish();
            return;
        }
        
        initializeViews();
        setupListeners();
        loadMonthlyReport(); // Cargar reporte mensual por defecto
    }
    
    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvPeriod = findViewById(R.id.tvPeriod);
        
        // Estadísticas principales
        tvTotalEarnings = findViewById(R.id.tvTotalEarnings);
        tvCompletedRequests = findViewById(R.id.tvCompletedRequests);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        
        // Métricas de rendimiento
        tvCompletionRate = findViewById(R.id.tvCompletionRate);
        tvCustomerSatisfaction = findViewById(R.id.tvCustomerSatisfaction);
        tvEfficiencyScore = findViewById(R.id.tvEfficiencyScore);
        
        // Tendencias
        tvEarningsGrowth = findViewById(R.id.tvEarningsGrowth);
        tvRatingTrend = findViewById(R.id.tvRatingTrend);
        tvRequestsGrowth = findViewById(R.id.tvRequestsGrowth);
        
        // Servicios y clientes
        tvMostRequestedService = findViewById(R.id.tvMostRequestedService);
        tvUniqueClients = findViewById(R.id.tvUniqueClients);
        tvReturningClients = findViewById(R.id.tvReturningClients);
        
        // Botones
        btnDaily = findViewById(R.id.btnDaily);
        btnWeekly = findViewById(R.id.btnWeekly);
        btnMonthly = findViewById(R.id.btnMonthly);
        btnYearly = findViewById(R.id.btnYearly);
        btnExport = findViewById(R.id.btnExport);
        
        // Layouts
        layoutStats = findViewById(R.id.layoutStats);
        layoutTrends = findViewById(R.id.layoutTrends);
        layoutServices = findViewById(R.id.layoutServices);
        layoutClients = findViewById(R.id.layoutClients);
        
        // Configurar título
        tvTitle.setText("📊 Reportes de " + currentUser.getName());
    }
    
    private void setupListeners() {
        btnDaily.setOnClickListener(v -> loadDailyReport());
        btnWeekly.setOnClickListener(v -> loadWeeklyReport());
        btnMonthly.setOnClickListener(v -> loadMonthlyReport());
        btnYearly.setOnClickListener(v -> loadYearlyReport());
        btnExport.setOnClickListener(v -> exportReport());
    }
    
    private void loadDailyReport() {
        updateButtonSelection(btnDaily);
        currentReport = reportGenerator.generateDailyReport(currentUser.getId());
        displayReport();
    }
    
    private void loadWeeklyReport() {
        updateButtonSelection(btnWeekly);
        currentReport = reportGenerator.generateWeeklyReport(currentUser.getId());
        displayReport();
    }
    
    private void loadMonthlyReport() {
        updateButtonSelection(btnMonthly);
        currentReport = reportGenerator.generateMonthlyReport(currentUser.getId());
        displayReport();
    }
    
    private void loadYearlyReport() {
        updateButtonSelection(btnYearly);
        currentReport = reportGenerator.generateYearlyReport(currentUser.getId());
        displayReport();
    }
    
    private void updateButtonSelection(Button selectedButton) {
        // Resetear todos los botones
        btnDaily.setBackgroundColor(getColor(R.color.white));
        btnWeekly.setBackgroundColor(getColor(R.color.white));
        btnMonthly.setBackgroundColor(getColor(R.color.white));
        btnYearly.setBackgroundColor(getColor(R.color.white));
        
        // Resaltar botón seleccionado
        selectedButton.setBackgroundColor(getColor(R.color.primary_light));
    }
    
    private void displayReport() {
        if (currentReport == null) {
            Toast.makeText(this, "Error al generar el reporte", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Mostrar período
        tvPeriod.setText(currentReport.getFormattedPeriod());
        
        // Estadísticas principales
        tvTotalEarnings.setText(currentReport.getFormattedTotalEarnings());
        tvCompletedRequests.setText(String.valueOf(currentReport.getCompletedRequests()));
        tvAverageRating.setText(currentReport.getFormattedAverageRating());
        
        // Métricas de rendimiento
        tvCompletionRate.setText(currentReport.getFormattedCompletionRate());
        tvCustomerSatisfaction.setText(currentReport.getFormattedCustomerSatisfaction());
        tvEfficiencyScore.setText(currentReport.getFormattedEfficiencyScore());
        
        // Tendencias
        displayTrends();
        
        // Servicios y clientes
        displayServicesAndClients();
        
        // Mostrar layouts
        layoutStats.setVisibility(View.VISIBLE);
        layoutTrends.setVisibility(View.VISIBLE);
        layoutServices.setVisibility(View.VISIBLE);
        layoutClients.setVisibility(View.VISIBLE);
    }
    
    private void displayTrends() {
        // Crecimiento de ganancias
        String earningsGrowthText = currentReport.getEarningsGrowthText();
        tvEarningsGrowth.setText(earningsGrowthText);
        if (currentReport.getEarningsGrowth() > 0) {
            tvEarningsGrowth.setTextColor(getColor(R.color.success));
        } else if (currentReport.getEarningsGrowth() < 0) {
            tvEarningsGrowth.setTextColor(getColor(R.color.error));
        } else {
            tvEarningsGrowth.setTextColor(getColor(R.color.text_secondary));
        }
        
        // Tendencia de calificaciones
        String ratingTrendText = currentReport.getRatingTrendText();
        tvRatingTrend.setText(ratingTrendText);
        if (currentReport.getRatingTrend() > 0) {
            tvRatingTrend.setTextColor(getColor(R.color.success));
        } else if (currentReport.getRatingTrend() < 0) {
            tvRatingTrend.setTextColor(getColor(R.color.error));
        } else {
            tvRatingTrend.setTextColor(getColor(R.color.text_secondary));
        }
        
        // Crecimiento de solicitudes
        String requestsGrowthText = currentReport.getRequestsGrowthText();
        tvRequestsGrowth.setText(requestsGrowthText);
        if (currentReport.getRequestsGrowth() > 0) {
            tvRequestsGrowth.setTextColor(getColor(R.color.success));
        } else if (currentReport.getRequestsGrowth() < 0) {
            tvRequestsGrowth.setTextColor(getColor(R.color.error));
        } else {
            tvRequestsGrowth.setTextColor(getColor(R.color.text_secondary));
        }
    }
    
    private void displayServicesAndClients() {
        // Servicio más solicitado
        String mostRequested = currentReport.getMostRequestedServiceName();
        if (mostRequested != null && !mostRequested.isEmpty()) {
            tvMostRequestedService.setText(mostRequested);
        } else {
            tvMostRequestedService.setText("N/A");
        }
        
        // Clientes únicos
        tvUniqueClients.setText(String.valueOf(currentReport.getUniqueClients()));
        
        // Clientes recurrentes
        tvReturningClients.setText(String.valueOf(currentReport.getReturningClients()));
    }
    
    private void exportReport() {
        if (currentReport == null) {
            Toast.makeText(this, "No hay reporte para exportar", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // TODO: Implementar exportación de reportes
        Toast.makeText(this, "Funcionalidad de exportación próximamente", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
