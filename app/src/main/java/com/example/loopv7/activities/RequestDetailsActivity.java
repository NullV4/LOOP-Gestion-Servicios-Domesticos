package com.example.loopv7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class RequestDetailsActivity extends AppCompatActivity {

    private TextView tvRequestId, tvServiceName, tvClientName, tvDate, tvTime, tvAddress, tvNotes, tvPrice, tvStatus;
    private Button btnAccept, btnReject, btnComplete, btnPay, btnRate;
    
    private SimpleDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Request request;
    private Service service;
    private User client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        
        // Inicializar componentes
        databaseHelper = new SimpleDatabaseHelper(this);
        sessionManager = new SessionManager(this);
        
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
        tvStatus.setText(getStatusText(request.getStatus()));
        
        // Configurar botones según el estado y rol
        setupButtons();
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
                btnComplete.setVisibility(View.VISIBLE);
                btnComplete.setText("✅ Marcar como Completado");
            }
        } else {
            // Para clientes
            if ("aceptada".equals(status) && "pendiente".equals(request.getPaymentStatus())) {
                btnPay.setVisibility(View.VISIBLE);
                btnPay.setText("💳 Pagar Servicio");
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
        request.setStatus("aceptada");
        request.setSociaId(sessionManager.getCurrentUserId());
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, "Solicitud aceptada", Toast.LENGTH_SHORT).show();
            setupButtons(); // Actualizar botones
        } else {
            Toast.makeText(this, "Error al aceptar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void rejectRequest() {
        request.setStatus("rechazada");
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, "Solicitud rechazada", Toast.LENGTH_SHORT).show();
            setupButtons(); // Actualizar botones
        } else {
            Toast.makeText(this, "Error al rechazar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void completeRequest() {
        request.setStatus("completada");
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, "Servicio marcado como completado", Toast.LENGTH_SHORT).show();
            setupButtons(); // Actualizar botones
        } else {
            Toast.makeText(this, "Error al completar el servicio", Toast.LENGTH_SHORT).show();
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
