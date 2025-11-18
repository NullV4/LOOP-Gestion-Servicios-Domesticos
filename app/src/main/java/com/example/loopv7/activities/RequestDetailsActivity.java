package com.example.loopv7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private TextView tvSociaName, tvSociaPhone, tvSociaRating;
    private ProgressBar progressBar;
    private Button btnAccept, btnReject, btnComplete, btnPay, btnRate, btnEdit, btnDelete;
    
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private NotificationHelper notificationHelper;
    private LocationHelper locationHelper;
    private Request request;
    private Service service;
    private User client;
    private User socia;

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
        
        // Información de la socia
        tvSociaName = findViewById(R.id.tvSociaName);
        tvSociaPhone = findViewById(R.id.tvSociaPhone);
        tvSociaRating = findViewById(R.id.tvSociaRating);
        
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnComplete = findViewById(R.id.btnComplete);
        btnPay = findViewById(R.id.btnPay);
        btnRate = findViewById(R.id.btnRate);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
    }
    
    private void loadRequestDetails(int requestId) {
        Log.d("RequestDetailsActivity", "Cargando detalles de solicitud ID: " + requestId);
        
        // Log del estado de la base de datos
        databaseHelper.logDatabaseStatus();
        
        request = databaseHelper.getRequestById(requestId);
        if (request == null) {
            Log.e("RequestDetailsActivity", "No se encontró la solicitud con ID: " + requestId);
            Toast.makeText(this, "Solicitud no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        Log.d("RequestDetailsActivity", "Solicitud encontrada: " + request.getId() + " - Estado: " + request.getStatus());
        
        service = databaseHelper.getServiceById(request.getServiceId());
        client = databaseHelper.getUserById(request.getClientId());
        
        // Cargar información de la socia si la solicitud fue aceptada
        if (request.getSociaId() > 0) {
            socia = databaseHelper.getUserById(request.getSociaId());
        }
        
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
        
        // Mostrar información de la socia si está disponible
        showSociaInfo();
        
        // Mostrar estado con ícono y progreso
        updateStatusDisplay();
        
        // Configurar botones según el estado y rol
        setupButtons();
    }
    
    /**
     * Muestra la información de la socia si la solicitud fue aceptada
     */
    private void showSociaInfo() {
        if (socia != null && request.getSociaId() > 0) {
            // Mostrar información de la socia
            tvSociaName.setText("👩‍💼 " + socia.getName());
            tvSociaPhone.setText("📞 " + socia.getPhone());
            tvSociaRating.setText("⭐ " + socia.getFormattedRating());
            
            // Hacer visible la sección de información de la socia
            findViewById(R.id.layoutSociaInfo).setVisibility(View.VISIBLE);
        } else {
            // Ocultar información de la socia
            findViewById(R.id.layoutSociaInfo).setVisibility(View.GONE);
        }
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
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        
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
            
            // Mostrar botones de editar y eliminar para solicitudes pendientes o rechazadas
            if ("pendiente".equals(status) || "rechazada".equals(status)) {
                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnEdit.setText("✏️ Editar Solicitud");
                btnDelete.setText("🗑️ Eliminar Solicitud");
            }
        }
    }
    
    private void setupListeners() {
        btnAccept.setOnClickListener(v -> acceptRequest());
        btnReject.setOnClickListener(v -> rejectRequest());
        btnComplete.setOnClickListener(v -> completeRequest());
        btnPay.setOnClickListener(v -> payService());
        btnRate.setOnClickListener(v -> rateService());
        btnEdit.setOnClickListener(v -> editRequest());
        btnDelete.setOnClickListener(v -> deleteRequest());
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
            
            // Cuando se completa el servicio, actualizar estadísticas de la socia
            updateSociaStats();
            
            // Si ya tiene calificación, archivar automáticamente
            if (request.getRating() > 0) {
                request.setArchived(true);
                message += ". La solicitud ha sido archivada (ya estaba calificada)";
            }
        }
        
        request.setStatus(newStatus);
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            
            // Enviar notificación al cliente
            notificationHelper.notifyRequestStatusChange(request, oldStatus, newStatus);
            
            // Si se completó el servicio, enviar notificación especial a la socia
            if ("completada".equals(newStatus)) {
                notificationHelper.notifyServiceCompletedToSocia(request);
            }
            
            // Actualizar visualización del estado
            updateStatusDisplay();
            setupButtons(); // Actualizar botones
        } else {
            Toast.makeText(this, "Error al actualizar el servicio", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Actualiza las estadísticas de la socia cuando completa un servicio
     */
    private void updateSociaStats() {
        try {
            User socia = databaseHelper.getUserById(request.getSociaId());
            if (socia != null && socia.isSocia()) {
                // Incrementar contador de servicios completados
                int completedServices = socia.getCompletedServices() + 1;
                socia.setCompletedServices(completedServices);
                
                // Actualizar fecha de último servicio
                socia.setLastServiceDate(databaseHelper.getCurrentDateTime());
                
                // Actualizar en la base de datos
                databaseHelper.updateUser(socia);
                
                Log.d("RequestDetailsActivity", "Estadísticas de socia actualizadas: " + completedServices + " servicios completados");
            }
        } catch (Exception e) {
            Log.e("RequestDetailsActivity", "Error actualizando estadísticas de socia", e);
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
    
    /**
     * Edita la solicitud (solo para clientes y solicitudes pendientes/rechazadas)
     */
    private void editRequest() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) return;
        
        // Solo clientes pueden editar sus propias solicitudes
        if (!currentUser.isCliente() || currentUser.getId() != request.getClientId()) {
            Toast.makeText(this, "Solo puedes editar tus propias solicitudes", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Solo se pueden editar solicitudes pendientes o rechazadas
        if (!"pendiente".equals(request.getStatus()) && !"rechazada".equals(request.getStatus())) {
            Toast.makeText(this, "Solo se pueden editar solicitudes pendientes o rechazadas", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Navegar a CreateRequestActivity con los datos de la solicitud
        Intent intent = new Intent(this, CreateRequestActivity.class);
        intent.putExtra("edit_request_id", request.getId());
        intent.putExtra("service_id", request.getServiceId());
        intent.putExtra("scheduled_date", request.getScheduledDate());
        intent.putExtra("scheduled_time", request.getScheduledTime());
        intent.putExtra("address", request.getAddress());
        intent.putExtra("notes", request.getNotes());
        startActivity(intent);
    }
    
    /**
     * Elimina la solicitud (solo para clientes y solicitudes pendientes/rechazadas)
     */
    private void deleteRequest() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) return;
        
        // Solo clientes pueden eliminar sus propias solicitudes
        if (!currentUser.isCliente() || currentUser.getId() != request.getClientId()) {
            Toast.makeText(this, "Solo puedes eliminar tus propias solicitudes", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Solo se pueden eliminar solicitudes pendientes o rechazadas
        if (!"pendiente".equals(request.getStatus()) && !"rechazada".equals(request.getStatus())) {
            Toast.makeText(this, "Solo se pueden eliminar solicitudes pendientes o rechazadas", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Mostrar diálogo de confirmación
        new android.app.AlertDialog.Builder(this)
            .setTitle("Eliminar Solicitud")
            .setMessage("¿Estás seguro de que quieres eliminar esta solicitud? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar", (dialog, which) -> {
                if (databaseHelper.deleteRequest(request.getId())) {
                    Toast.makeText(this, "Solicitud eliminada exitosamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad
                } else {
                    Toast.makeText(this, "Error al eliminar la solicitud", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
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
