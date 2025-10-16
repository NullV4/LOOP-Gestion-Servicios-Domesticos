package com.example.loopv7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.NotificationHelper;
import com.example.loopv7.utils.SessionManager;
import com.example.loopv7.utils.LocationHelper;
import com.example.loopv7.services.LocationTrackingService;

public class RequestDetailsActivity extends AppCompatActivity {

    private TextView tvRequestId, tvServiceName, tvClientName, tvDate, tvTime, tvAddress, tvNotes, tvPrice, tvStatus, tvStatusIcon, tvProgressText;
    private ProgressBar progressBar;
    private Button btnAccept, btnReject, btnComplete, btnPay, btnRate;
    
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private NotificationHelper notificationHelper;
    private LocationHelper locationHelper;
    private Request request;
    private Service service;
    private User client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        
        // Inicializar componentes
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        notificationHelper = new NotificationHelper(this);
        locationHelper = new LocationHelper(this, this);
        
        // Obtener ID de la solicitud
        int requestId = getIntent().getIntExtra("request_id", -1);
        if (requestId == -1) {
            Toast.makeText(this, "Error: ID de solicitud no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        loadRequestDetails(requestId);
        setupListeners();
    }
    
    private void initializeViews() {
        tvRequestId = findViewById(R.id.tvRequestId);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvClientName = findViewById(R.id.tvClientName);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvAddress = findViewById(R.id.tvAddress);
        tvNotes = findViewById(R.id.tvNotes);
        tvPrice = findViewById(R.id.tvPrice);
        tvStatus = findViewById(R.id.tvStatus);
        tvStatusIcon = findViewById(R.id.tvStatusIcon);
        tvProgressText = findViewById(R.id.tvProgressText);
        progressBar = findViewById(R.id.progressBar);
        
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnComplete = findViewById(R.id.btnComplete);
        btnPay = findViewById(R.id.btnPay);
        btnRate = findViewById(R.id.btnRate);
    }
    
    private void loadRequestDetails(int requestId) {
        request = databaseHelper.getRequestById(requestId);
        if (request == null) {
            Toast.makeText(this, "Solicitud no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        service = databaseHelper.getServiceById(request.getServiceId());
        client = databaseHelper.getUserById(request.getClientId());
        
        if (service == null || client == null) {
            Toast.makeText(this, "Error al cargar datos de la solicitud", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Mostrar información
        tvRequestId.setText("Solicitud #" + request.getId());
        tvServiceName.setText(service.getName());
        tvClientName.setText(client.getName());
        tvDate.setText(request.getScheduledDate());
        tvTime.setText(request.getScheduledTime());
        tvAddress.setText(request.getAddress());
        tvNotes.setText(request.getNotes() != null ? request.getNotes() : "Sin notas adicionales");
        tvPrice.setText(request.getFormattedPrice());
        
        // Mostrar estado con ícono y progreso
        updateStatusDisplay();
        
        // Configurar botones según el estado y rol
        setupButtons();
    }
    
    /**
     * Actualiza la visualización del estado con ícono y progreso
     */
    private void updateStatusDisplay() {
        String status = request.getStatus();
        
        // Actualizar ícono y texto del estado
        tvStatusIcon.setText(request.getStatusIcon());
        tvStatus.setText(request.getStatusText());
        
        // Configurar color del estado
        int colorRes = getColorForStatus(status);
        tvStatus.setTextColor(getColor(colorRes));
        
        // Mostrar/ocultar elementos de progreso
        if ("en_progreso".equals(status)) {
            progressBar.setVisibility(View.VISIBLE);
            tvProgressText.setVisibility(View.VISIBLE);
            progressBar.setProgress(50); // Simular progreso al 50%
            tvProgressText.setText("🔄 El servicio está en progreso...");
        } else {
            progressBar.setVisibility(View.GONE);
            tvProgressText.setVisibility(View.GONE);
        }
    }
    
    /**
     * Obtiene el color del recurso para el estado
     */
    private int getColorForStatus(String status) {
        switch (status) {
            case "pendiente": return R.color.warning;
            case "aceptada": return R.color.info;
            case "rechazada": return R.color.error;
            case "en_progreso": return R.color.warning;
            case "completada": return R.color.success;
            case "cancelada": return R.color.text_secondary;
            default: return R.color.text_primary;
        }
    }
    
    private void setupButtons() {
        String status = request.getStatus();
        boolean isSocia = sessionManager.isSocia();
        
        // Ocultar todos los botones por defecto
        btnAccept.setVisibility(View.GONE);
        btnReject.setVisibility(View.GONE);
        btnComplete.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
        btnRate.setVisibility(View.GONE);
        
        if (isSocia) {
            // Para socias
            if ("pendiente".equals(status)) {
                btnAccept.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                btnAccept.setText("✅ Aceptar Solicitud");
                btnReject.setText("❌ Rechazar Solicitud");
            } else if ("aceptada".equals(status) && request.getSociaId() == sessionManager.getCurrentUserId()) {
                // Verificar si el pago está realizado antes de permitir iniciar
                if ("pagado".equals(request.getPaymentStatus())) {
                    btnComplete.setVisibility(View.VISIBLE);
                    btnComplete.setText("🚀 Iniciar Servicio");
                }
            } else if ("en_progreso".equals(status) && request.getSociaId() == sessionManager.getCurrentUserId()) {
                btnComplete.setVisibility(View.VISIBLE);
                btnComplete.setText("✅ Completar Servicio");
            }
        } else {
            // Para clientes
            if ("aceptada".equals(status) && "pendiente".equals(request.getPaymentStatus())) {
                btnPay.setVisibility(View.VISIBLE);
                btnPay.setText("💳 Pagar Servicio");
            } else if ("en_progreso".equals(status)) {
                // Mostrar información de que el servicio está en progreso
                // No hay botones para el cliente en este estado
            } else if ("completada".equals(status) && "pagado".equals(request.getPaymentStatus()) && request.getRating() == 0) {
                btnRate.setVisibility(View.VISIBLE);
                btnRate.setText("⭐ Calificar Servicio");
            }
        }
    }
    
    private void setupListeners() {
        btnAccept.setOnClickListener(v -> acceptRequest());
        btnReject.setOnClickListener(v -> rejectRequest());
        btnComplete.setOnClickListener(v -> completeRequest());
        btnPay.setOnClickListener(v -> payService());
        btnRate.setOnClickListener(v -> rateService());
    }
    
    private void acceptRequest() {
        String oldStatus = request.getStatus();
        request.setStatus("aceptada");
        request.setSociaId(sessionManager.getCurrentUserId());
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, "Solicitud aceptada", Toast.LENGTH_SHORT).show();
            
            // Enviar notificación al cliente
            notificationHelper.notifyRequestStatusChange(request, oldStatus, "aceptada");
            
            setupButtons(); // Actualizar botones
        } else {
            Toast.makeText(this, "Error al aceptar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void rejectRequest() {
        String oldStatus = request.getStatus();
        request.setStatus("rechazada");
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, "Solicitud rechazada", Toast.LENGTH_SHORT).show();
            
            // Enviar notificación al cliente
            notificationHelper.notifyRequestStatusChange(request, oldStatus, "rechazada");
            
            setupButtons(); // Actualizar botones
        } else {
            Toast.makeText(this, "Error al rechazar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void completeRequest() {
        String oldStatus = request.getStatus();
        String newStatus;
        String message;
        
        if ("aceptada".equals(oldStatus)) {
            // Cambiar de "aceptada" a "en_progreso"
            newStatus = "en_progreso";
            message = "Servicio iniciado";
        } else {
            // Cambiar de "en_progreso" a "completada"
            newStatus = "completada";
            message = "Servicio marcado como completado";
        }
        
        request.setStatus(newStatus);
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            
            // Enviar notificación al cliente
            notificationHelper.notifyRequestStatusChange(request, oldStatus, newStatus);
            
            // Actualizar visualización del estado
            updateStatusDisplay();
            setupButtons(); // Actualizar botones
        } else {
            Toast.makeText(this, "Error al actualizar el servicio", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void payService() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("request_id", request.getId());
        startActivity(intent);
    }
    
    private void rateService() {
        Intent intent = new Intent(this, RateServiceActivity.class);
        intent.putExtra("request_id", request.getId());
        startActivity(intent);
    }
    
    private String getStatusText(String status) {
        switch (status) {
            case "pendiente": return "Pendiente";
            case "aceptada": return "Aceptada";
            case "rechazada": return "Rechazada";
            case "completada": return "Completada";
            case "cancelada": return "Cancelada";
            default: return status;
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar los datos cuando se regresa de PaymentActivity o RateServiceActivity
        if (request != null) {
            loadRequestDetails(request.getId());
        }
    }
}
