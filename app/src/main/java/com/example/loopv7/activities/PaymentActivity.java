package com.example.loopv7.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvServiceName, tvSociaName, tvTotalAmount, tvPaymentStatus;
    private EditText etCardNumber, etExpiryDate, etCVV, etCardHolder;
    private Button btnProcessPayment;
    
    private SimpleDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Request request;
    private Service service;
    private User socia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
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
            Toast.makeText(this, "Solo los clientes pueden realizar pagos", Toast.LENGTH_SHORT).show();
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
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus);
        
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCVV = findViewById(R.id.etCVV);
        etCardHolder = findViewById(R.id.etCardHolder);
        
        btnProcessPayment = findViewById(R.id.btnProcessPayment);
    }
    
    private void loadRequestDetails(int requestId) {
        request = databaseHelper.getRequestById(requestId);
        if (request == null) {
            Toast.makeText(this, "Solicitud no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Verificar que la solicitud esté aceptada
        if (!"aceptada".equals(request.getStatus())) {
            Toast.makeText(this, "Solo se pueden pagar servicios aceptados", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Verificar que no esté ya pagado
        if ("pagado".equals(request.getPaymentStatus())) {
            Toast.makeText(this, "Este servicio ya fue pagado", Toast.LENGTH_SHORT).show();
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
        tvSociaName.setText("Servicio por: " + socia.getName());
        tvTotalAmount.setText(request.getFormattedPrice());
    }
    
    private void setupListeners() {
        btnProcessPayment.setOnClickListener(v -> processPayment());
    }
    
    private void processPayment() {
        String cardNumber = etCardNumber.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();
        String cvv = etCVV.getText().toString().trim();
        String cardHolder = etCardHolder.getText().toString().trim();
        
        // Validaciones
        if (TextUtils.isEmpty(cardNumber)) {
            etCardNumber.setError("Número de tarjeta requerido");
            etCardNumber.requestFocus();
            return;
        }
        
        if (cardNumber.length() < 16) {
            etCardNumber.setError("Número de tarjeta inválido");
            etCardNumber.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(expiryDate)) {
            etExpiryDate.setError("Fecha de vencimiento requerida");
            etExpiryDate.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(cvv)) {
            etCVV.setError("CVV requerido");
            etCVV.requestFocus();
            return;
        }
        
        if (cvv.length() < 3) {
            etCVV.setError("CVV inválido");
            etCVV.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(cardHolder)) {
            etCardHolder.setError("Nombre del titular requerido");
            etCardHolder.requestFocus();
            return;
        }
        
        // Simular procesamiento de pago
        simulatePayment();
    }
    
    private void simulatePayment() {
        btnProcessPayment.setEnabled(false);
        btnProcessPayment.setText("Procesando...");
        tvPaymentStatus.setText("Procesando pago...");
        tvPaymentStatus.setVisibility(View.VISIBLE);
        
        // Simular delay de procesamiento
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Simular éxito del pago (90% de éxito)
            boolean paymentSuccess = Math.random() > 0.1;
            
            if (paymentSuccess) {
                // Actualizar estado de pago
                request.setPaymentStatus("pagado");
                
                if (databaseHelper.updateRequest(request)) {
                    tvPaymentStatus.setText("✅ Pago procesado exitosamente");
                    tvPaymentStatus.setTextColor(getColor(R.color.success));
                    
                    Toast.makeText(this, "¡Pago realizado con éxito!", Toast.LENGTH_SHORT).show();
                    
                    // Deshabilitar campos
                    etCardNumber.setEnabled(false);
                    etExpiryDate.setEnabled(false);
                    etCVV.setEnabled(false);
                    etCardHolder.setEnabled(false);
                    btnProcessPayment.setText("Pago Completado");
                    
                    // Cerrar actividad después de un delay
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        finish();
                    }, 2000);
                } else {
                    showPaymentError("Error al actualizar el estado del pago");
                }
            } else {
                showPaymentError("Pago rechazado. Verifique los datos de su tarjeta.");
            }
        }, 3000);
    }
    
    private void showPaymentError(String message) {
        tvPaymentStatus.setText("❌ " + message);
        tvPaymentStatus.setTextColor(getColor(R.color.error));
        btnProcessPayment.setEnabled(true);
        btnProcessPayment.setText("Reintentar Pago");
    }
}
