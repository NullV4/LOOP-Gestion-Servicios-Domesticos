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
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.models.Rating;
import com.example.loopv7.utils.NotificationHelper;
import com.example.loopv7.utils.SessionManager;
import com.example.loopv7.utils.ValidationHelper;

public class RateServiceActivity extends AppCompatActivity {

    private TextView tvServiceName, tvSociaName;
    private RatingBar ratingBar;
    private EditText etReview;
    private Button btnSubmitRating;
    
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private NotificationHelper notificationHelper;
    private ValidationHelper validationHelper;
    private Request request;
    private Service service;
    private User socia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_service);
        
        // Inicializar componentes
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        notificationHelper = new NotificationHelper(this);
        validationHelper = new ValidationHelper(this);
        
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
        
        // Validaciones robustas usando ValidationHelper
        boolean isValid = true;
        
        // Validar calificación
        ValidationHelper.ValidationResult ratingResult = validationHelper.validateRating(rating);
        if (!ratingResult.isValid()) {
            Toast.makeText(this, ratingResult.getMessage(), Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        
        // Validar reseña
        ValidationHelper.ValidationResult reviewResult = validationHelper.validateReview(review);
        if (!reviewResult.isValid()) {
            etReview.setError(reviewResult.getMessage());
            etReview.requestFocus();
            isValid = false;
        } else {
            etReview.setError(null);
        }
        
        if (!isValid) {
            return;
        }
        
        // Crear registro de calificación detallada en la tabla ratings
        Rating ratingRecord = new Rating();
        ratingRecord.setRequestId(request.getId());
        ratingRecord.setRaterId(sessionManager.getCurrentUserId());
        ratingRecord.setRatedId(request.getSociaId());
        ratingRecord.setOverallRating(rating);
        ratingRecord.setQualityRating(rating); // Por simplicidad, usar la misma calificación
        ratingRecord.setPunctualityRating(rating);
        ratingRecord.setCommunicationRating(rating);
        ratingRecord.setCleanlinessRating(rating);
        ratingRecord.setReview(review);
        ratingRecord.setAnonymous(false);
        ratingRecord.setStatus("activo");
        ratingRecord.setCreatedAt(databaseHelper.getCurrentDateTime());
        
        // Insertar calificación en la base de datos
        long ratingId = databaseHelper.insertRating(ratingRecord);
        
        if (ratingId != -1) {
            // Actualizar la solicitud con la calificación básica (para compatibilidad)
            request.setRating(rating);
            request.setReview(review);
            
            if (databaseHelper.updateRequest(request)) {
                Toast.makeText(this, "¡Gracias por tu calificación!", Toast.LENGTH_SHORT).show();
                
                // Enviar notificación a la socia
                notificationHelper.notifyRatingReceived(request, rating);
                
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar la solicitud", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error al registrar la calificación", Toast.LENGTH_SHORT).show();
        }
    }
}
