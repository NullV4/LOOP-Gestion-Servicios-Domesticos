package com.example.loopv7.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.models.Payment;
import com.example.loopv7.utils.NotificationHelper;
import com.example.loopv7.utils.SessionManager;
import com.example.loopv7.utils.ValidationHelper;
import com.example.loopv7.utils.ErrorHandler;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvServiceName, tvSociaName, tvTotalAmount, tvPaymentStatus;
    private EditText etCardNumber, etExpiryDate, etCVV, etCardHolder;
    private Button btnProcessPayment;
    
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private NotificationHelper notificationHelper;
    private ValidationHelper validationHelper;
    private ErrorHandler errorHandler;
    private Request request;
    private Service service;
    private User socia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
        // Inicializar componentes
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        notificationHelper = new NotificationHelper(this);
        validationHelper = new ValidationHelper(this);
        errorHandler = ErrorHandler.getInstance(this);
        
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
        
        // Configurar formateo automático para fecha de vencimiento (MM/YY)
        setupExpiryDateFormatter();
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
        
        // Validación robusta de datos de tarjeta
        ValidationHelper.ValidationResult cardResult = validationHelper.validateCardData(cardNumber, expiryDate, cvv, cardHolder);
        if (!cardResult.isValid()) {
            // Mostrar error en el campo correspondiente
            if (cardResult.getMessage().contains("número de tarjeta")) {
                etCardNumber.setError(cardResult.getMessage());
                etCardNumber.requestFocus();
            } else if (cardResult.getMessage().contains("fecha de vencimiento")) {
                etExpiryDate.setError(cardResult.getMessage());
                etExpiryDate.requestFocus();
            } else if (cardResult.getMessage().contains("CVV")) {
                etCVV.setError(cardResult.getMessage());
                etCVV.requestFocus();
            } else if (cardResult.getMessage().contains("titular")) {
                etCardHolder.setError(cardResult.getMessage());
                etCardHolder.requestFocus();
            } else {
                Toast.makeText(this, cardResult.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        
        // Validación adicional para fecha de vencimiento con formato MM/YY
        if (!isValidExpiryDate(expiryDate)) {
            etExpiryDate.setError("Fecha de vencimiento inválida o expirada");
            etExpiryDate.requestFocus();
            return;
        }
        
        // Limpiar errores si la validación es exitosa
        etCardNumber.setError(null);
        etExpiryDate.setError(null);
        etCVV.setError(null);
        etCardHolder.setError(null);
        
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
                // Crear registro de pago en la tabla payments
                Payment payment = new Payment();
                payment.setRequestId(request.getId());
                payment.setAmount(request.getTotalPrice());
                payment.setCurrency("MXN");
                payment.setPaymentMethod("tarjeta_credito");
                payment.setPaymentStatus("completado");
                payment.setTransactionId("TXN-" + System.currentTimeMillis());
                payment.setGatewayName("LOOP_PAY");
                payment.setGatewayResponse("SUCCESS");
                payment.setPaymentDate(databaseHelper.getCurrentDateTime());
                payment.setProcessedAt(databaseHelper.getCurrentDateTime());
                payment.setNotes("Pago procesado exitosamente");
                
                // Insertar pago en la base de datos
                long paymentId = databaseHelper.insertPayment(payment);
                
                if (paymentId != -1) {
                    // Actualizar estado de pago en la solicitud
                    request.setPaymentStatus("pagado");
                    
                    if (databaseHelper.updateRequest(request)) {
                        tvPaymentStatus.setText("✅ Pago procesado exitosamente");
                        tvPaymentStatus.setTextColor(getColor(R.color.success));
                        
                        Toast.makeText(this, "¡Pago realizado con éxito!", Toast.LENGTH_SHORT).show();
                        
                        // Enviar notificación a la socia
                        notificationHelper.notifyPaymentReceived(request);
                        
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
                        errorHandler.handleError(ErrorHandler.ErrorType.DATABASE_ERROR, 
                            "Error al actualizar el estado del pago");
                        showPaymentError("Error al actualizar el estado del pago");
                    }
                } else {
                    errorHandler.handleError(ErrorHandler.ErrorType.DATABASE_ERROR, 
                        "Error al registrar el pago");
                    showPaymentError("Error al registrar el pago");
                }
            } else {
                errorHandler.handleError(ErrorHandler.ErrorType.BUSINESS_LOGIC_ERROR, 
                    "Pago rechazado por el procesador");
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
    
    /**
     * Configura el formateo automático para la fecha de vencimiento en formato MM/YY
     */
    private void setupExpiryDateFormatter() {
        etExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementar
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                
                // Remover caracteres no numéricos
                String digitsOnly = input.replaceAll("[^0-9]", "");
                
                // Limitar a 4 dígitos máximo
                if (digitsOnly.length() > 4) {
                    digitsOnly = digitsOnly.substring(0, 4);
                }
                
                // Formatear como MM/YY
                String formatted = "";
                if (digitsOnly.length() >= 1) {
                    formatted = digitsOnly.substring(0, 1);
                }
                if (digitsOnly.length() >= 2) {
                    formatted = digitsOnly.substring(0, 2);
                }
                if (digitsOnly.length() >= 3) {
                    formatted = digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2, 3);
                }
                if (digitsOnly.length() >= 4) {
                    formatted = digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2, 4);
                }
                
                // Actualizar el texto si ha cambiado
                if (!input.equals(formatted)) {
                    etExpiryDate.removeTextChangedListener(this);
                    etExpiryDate.setText(formatted);
                    etExpiryDate.setSelection(formatted.length());
                    etExpiryDate.addTextChangedListener(this);
                }
                
                // Validar mes (01-12)
                if (digitsOnly.length() >= 2) {
                    int month = Integer.parseInt(digitsOnly.substring(0, 2));
                    if (month < 1 || month > 12) {
                        etExpiryDate.setError("Mes inválido (01-12)");
                    } else {
                        etExpiryDate.setError(null);
                    }
                }
            }
        });
        
        // Configurar hint y tipo de entrada
        etExpiryDate.setHint("MM/YY");
        etExpiryDate.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
    }
    
    /**
     * Valida el formato MM/YY de la fecha de vencimiento
     * @param expiryDate Fecha en formato MM/YY
     * @return true si es válida, false si no
     */
    private boolean isValidExpiryDate(String expiryDate) {
        if (TextUtils.isEmpty(expiryDate)) {
            return false;
        }
        
        // Verificar formato MM/YY
        String pattern = "^(0[1-9]|1[0-2])/([0-9]{2})$";
        if (!expiryDate.matches(pattern)) {
            return false;
        }
        
        // Extraer mes y año
        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        
        // Validar mes (01-12)
        if (month < 1 || month > 12) {
            return false;
        }
        
        // Validar que no sea una fecha pasada
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int currentYear = calendar.get(java.util.Calendar.YEAR) % 100; // Obtener últimos 2 dígitos
        int currentMonth = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH es 0-based
        
        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return false;
        }
        
        return true;
    }
}
