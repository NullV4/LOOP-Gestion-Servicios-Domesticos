package com.example.loopv7.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

import java.util.Calendar;
import java.util.List;

public class CreateRequestActivity extends AppCompatActivity {

    private Spinner spinnerService;
    private EditText etDate, etTime, etAddress, etNotes;
    private TextView tvTotalPrice;
    private Button btnCreateRequest;
    
    private SimpleDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private List<Service> services;
    private Service selectedService;
    private Calendar selectedDate;
    private int selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        
        // Inicializar componentes
        databaseHelper = new SimpleDatabaseHelper(this);
        sessionManager = new SessionManager(this);
        selectedDate = Calendar.getInstance();
        
        // Verificar que el usuario sea cliente
        if (!sessionManager.isCliente()) {
            Toast.makeText(this, "Solo los clientes pueden crear solicitudes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        loadServices();
        setupListeners();
    }
    
    private void initializeViews() {
        spinnerService = findViewById(R.id.spinnerService);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etAddress = findViewById(R.id.etAddress);
        etNotes = findViewById(R.id.etNotes);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCreateRequest = findViewById(R.id.btnCreateRequest);
        
        // Configurar campos como no editables
        etDate.setFocusable(false);
        etTime.setFocusable(false);
    }
    
    private void loadServices() {
        services = databaseHelper.getAllServices();
        
        if (services.isEmpty()) {
            Toast.makeText(this, "No hay servicios disponibles", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Crear adapter para el spinner
        ArrayAdapter<Service> adapter = new ArrayAdapter<Service>(this, android.R.layout.simple_spinner_item, services) {
            @Override
            public String toString() {
                return getItem(getPosition(getItem(getPosition(null)))).getName() + " - " + getItem(getPosition(getItem(getPosition(null)))).getFormattedPrice();
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(adapter);
        
        // Seleccionar primer servicio por defecto
        if (!services.isEmpty()) {
            selectedService = services.get(0);
            updateTotalPrice();
        }
        
        // Listener para cambio de servicio
        spinnerService.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedService = services.get(position);
                updateTotalPrice();
            }
            
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }
    
    private void setupListeners() {
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());
        
        btnCreateRequest.setOnClickListener(v -> createRequest());
    }
    
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    etDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH));
        
        // No permitir fechas pasadas
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    etTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                },
                selectedHour,
                selectedMinute,
                true);
        timePickerDialog.show();
    }
    
    private void updateTotalPrice() {
        if (selectedService != null) {
            tvTotalPrice.setText(selectedService.getFormattedPrice());
        }
    }
    
    private void createRequest() {
        // Validaciones
        if (selectedService == null) {
            Toast.makeText(this, "Seleccione un servicio", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(etDate.getText().toString())) {
            etDate.setError("Seleccione una fecha");
            etDate.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(etTime.getText().toString())) {
            etTime.setError("Seleccione una hora");
            etTime.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            etAddress.setError("La dirección es requerida");
            etAddress.requestFocus();
            return;
        }
        
        // Crear solicitud
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String address = etAddress.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        
        Request request = new Request(
                sessionManager.getCurrentUserId(),
                selectedService.getId(),
                date,
                time,
                address,
                notes,
                selectedService.getPrice()
        );
        
        long result = databaseHelper.insertRequest(request);
        
        if (result != -1) {
            Toast.makeText(this, "Solicitud creada exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}
