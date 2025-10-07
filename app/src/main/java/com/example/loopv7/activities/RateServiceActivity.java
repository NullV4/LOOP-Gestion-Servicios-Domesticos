package com.example.loopv7.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class RateServiceActivity extends AppCompatActivity {

    private TextView tvServiceName, tvSociaName;
    private RatingBar ratingBar;
    private EditText etReview;
    private Button btnSubmitRating;
    
    private SimpleDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Request request;
    private Service service;
    private User socia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_service);
        
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
        
        // Verificar que el usuario sea cliente
        if (!sessionManager.isCliente()) {
            Toast.makeText(this, "Solo los clientes pueden calificar servicios", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        loadRequestDetails(requestId);
        setupListeners();
    }
    
    private void initializeViews() {
        tvServiceName = findViewById(R.id.tvServiceName);
        tvSociaName = findViewById(R.id.tvSociaName);
        ratingBar = findViewById(R.id.ratingBar);
        etReview = findViewById(R.id.etReview);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);
    }
    
    private void loadRequestDetails(int requestId) {
        request = databaseHelper.getRequestById(requestId);
        if (request == null) {
            Toast.makeText(this, "Solicitud no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Verificar que la solicitud esté completada y pagada
        if (!"completada".equals(request.getStatus())) {
            Toast.makeText(this, "Solo se pueden calificar servicios completados", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        if (!"pagado".equals(request.getPaymentStatus())) {
            Toast.makeText(this, "Debe pagar el servicio antes de calificarlo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Verificar que no esté ya calificada
        if (request.getRating() > 0) {
            Toast.makeText(this, "Este servicio ya fue calificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        service = databaseHelper.getServiceById(request.getServiceId());
        socia = databaseHelper.getUserById(request.getSociaId());
        
        if (service == null || socia == null) {
            Toast.makeText(this, "Error al cargar datos del servicio", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Mostrar información
        tvServiceName.setText(service.getName());
        tvSociaName.setText("Servicio realizado por: " + socia.getName());
    }
    
    private void setupListeners() {
        btnSubmitRating.setOnClickListener(v -> submitRating());
    }
    
    private void submitRating() {
        int rating = (int) ratingBar.getRating();
        String review = etReview.getText().toString().trim();
        
        // Validaciones
        if (rating == 0) {
            Toast.makeText(this, "Por favor seleccione una calificación", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(review)) {
            Toast.makeText(this, "Por favor escriba una reseña", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (review.length() < 10) {
            Toast.makeText(this, "La reseña debe tener al menos 10 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Actualizar la solicitud con la calificación
        request.setRating(rating);
        request.setReview(review);
        
        if (databaseHelper.updateRequest(request)) {
            Toast.makeText(this, "¡Gracias por tu calificación!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al enviar la calificación", Toast.LENGTH_SHORT).show();
        }
    }
}
