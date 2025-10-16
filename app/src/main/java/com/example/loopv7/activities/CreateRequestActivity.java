package com.example.loopv7.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;
import com.example.loopv7.utils.ValidationHelper;
import com.example.loopv7.utils.ErrorHandler;

import java.util.Calendar;
import java.util.List;

public class CreateRequestActivity extends AppCompatActivity {

    private Spinner spinnerService;
    private EditText etDate, etTime, etAddress, etNotes;
    private TextView tvTotalPrice;
    private Button btnCreateRequest;
    
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private ValidationHelper validationHelper;
    private ErrorHandler errorHandler;
    private List<Service> services;
    private Service selectedService;
    private Calendar selectedDate;
    private int selectedHour, selectedMinute;
    private boolean isEditMode = false;
    private int editRequestId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        
        // Inicializar componentes
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        validationHelper = new ValidationHelper(this);
        errorHandler = ErrorHandler.getInstance(this);
        selectedDate = Calendar.getInstance();
        
        // Verificar que el usuario sea cliente
        if (!sessionManager.isCliente()) {
            Toast.makeText(this, "Solo los clientes pueden crear solicitudes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Obtener service_id del intent si viene desde ServicesFragment
        int preselectedServiceId = getIntent().getIntExtra("service_id", -1);
        
        initializeViews();
        checkEditMode();
        loadServices(preselectedServiceId);
        setupListeners();
    }
    
    /**
     * Verifica si estamos en modo de edición y carga los datos correspondientes
     */
    private void checkEditMode() {
        editRequestId = getIntent().getIntExtra("edit_request_id", -1);
        if (editRequestId != -1) {
            isEditMode = true;
            
            // Cargar datos de la solicitud a editar
            Request request = databaseHelper.getRequestById(editRequestId);
            if (request != null) {
                // Pre-llenar los campos con los datos existentes
                etDate.setText(getIntent().getStringExtra("scheduled_date"));
                etTime.setText(getIntent().getStringExtra("scheduled_time"));
                etAddress.setText(getIntent().getStringExtra("address"));
                etNotes.setText(getIntent().getStringExtra("notes"));
                
                // Cambiar el título y texto del botón
                setTitle("Editar Solicitud");
                btnCreateRequest.setText("💾 Actualizar Solicitud");
            }
        }
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
    
    private void loadServices(int preselectedServiceId) {
        services = databaseHelper.getAllServices();
        
        if (services.isEmpty()) {
            Toast.makeText(this, "No hay servicios disponibles", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Crear adapter para el spinner con toString() simplificado
        ArrayAdapter<Service> adapter = new ArrayAdapter<Service>(this, android.R.layout.simple_spinner_item, services) {
            @Override
            public String toString() {
                // Este método no se usa para mostrar el texto en el spinner
                // El texto se muestra usando getView() o getDropDownView()
                return "";
            }
            
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                Service service = getItem(position);
                if (service != null) {
                    textView.setText(service.getName() + " - " + service.getFormattedPrice());
                }
                return view;
            }
            
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                Service service = getItem(position);
                if (service != null) {
                    textView.setText(service.getName() + " - " + service.getFormattedPrice());
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(adapter);
        
        // Seleccionar servicio preseleccionado o el primero por defecto
        int selectedPosition = 0;
        if (preselectedServiceId != -1) {
            // Buscar el servicio preseleccionado
            for (int i = 0; i < services.size(); i++) {
                if (services.get(i).getId() == preselectedServiceId) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        
        if (!services.isEmpty()) {
            selectedService = services.get(selectedPosition);
            spinnerService.setSelection(selectedPosition);
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
        
        btnCreateRequest.setOnClickListener(v -> {
            if (isEditMode) {
                updateRequest();
            } else {
                createRequest();
            }
        });
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
        // Validaciones robustas usando ValidationHelper
        boolean isValid = true;
        
        // Validar servicio seleccionado
        if (selectedService == null) {
            Toast.makeText(this, "Seleccione un servicio", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar fecha
        String dateString = etDate.getText().toString().trim();
        ValidationHelper.ValidationResult dateResult = validationHelper.validateDate(dateString, true);
        if (!dateResult.isValid()) {
            etDate.setError(dateResult.getMessage());
            etDate.requestFocus();
            isValid = false;
        } else {
            etDate.setError(null);
        }
        
        // Validar horario
        String timeString = etTime.getText().toString().trim();
        ValidationHelper.ValidationResult timeResult = validationHelper.validateTime(timeString);
        if (!timeResult.isValid()) {
            etTime.setError(timeResult.getMessage());
            etTime.requestFocus();
            isValid = false;
        } else {
            etTime.setError(null);
        }
        
        // Validar dirección
        String address = etAddress.getText().toString().trim();
        ValidationHelper.ValidationResult addressResult = validationHelper.validateAddress(address);
        if (!addressResult.isValid()) {
            etAddress.setError(addressResult.getMessage());
            etAddress.requestFocus();
            isValid = false;
        } else {
            etAddress.setError(null);
        }
        
        // Validar notas (opcional)
        String notes = etNotes.getText().toString().trim();
        ValidationHelper.ValidationResult notesResult = validationHelper.validateNotes(notes);
        if (!notesResult.isValid()) {
            etNotes.setError(notesResult.getMessage());
            etNotes.requestFocus();
            isValid = false;
        } else {
            etNotes.setError(null);
        }
        
        if (!isValid) {
            return;
        }
        
        // Crear solicitud
        try {
            String date = dateString;
            String time = timeString;
            
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
                errorHandler.handleError(ErrorHandler.ErrorType.DATABASE_ERROR, 
                    "No se pudo crear la solicitud en la base de datos");
            }
        } catch (Exception e) {
            errorHandler.handleCriticalError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al crear la solicitud", e);
        }
    }
    
    /**
     * Actualiza una solicitud existente
     */
    private void updateRequest() {
        // Validaciones robustas usando ValidationHelper (mismo código que createRequest)
        boolean isValid = true;
        
        // Validar servicio seleccionado
        if (selectedService == null) {
            Toast.makeText(this, "Seleccione un servicio", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar fecha
        String dateString = etDate.getText().toString().trim();
        ValidationHelper.ValidationResult dateResult = validationHelper.validateDate(dateString, true);
        if (!dateResult.isValid()) {
            etDate.setError(dateResult.getMessage());
            etDate.requestFocus();
            isValid = false;
        } else {
            etDate.setError(null);
        }
        
        // Validar horario
        String timeString = etTime.getText().toString().trim();
        ValidationHelper.ValidationResult timeResult = validationHelper.validateTime(timeString);
        if (!timeResult.isValid()) {
            etTime.setError(timeResult.getMessage());
            etTime.requestFocus();
            isValid = false;
        } else {
            etTime.setError(null);
        }
        
        // Validar dirección
        String address = etAddress.getText().toString().trim();
        ValidationHelper.ValidationResult addressResult = validationHelper.validateAddress(address);
        if (!addressResult.isValid()) {
            etAddress.setError(addressResult.getMessage());
            etAddress.requestFocus();
            isValid = false;
        } else {
            etAddress.setError(null);
        }
        
        // Validar notas (opcional)
        String notes = etNotes.getText().toString().trim();
        ValidationHelper.ValidationResult notesResult = validationHelper.validateNotes(notes);
        if (!notesResult.isValid()) {
            etNotes.setError(notesResult.getMessage());
            etNotes.requestFocus();
            isValid = false;
        } else {
            etNotes.setError(null);
        }
        
        if (!isValid) {
            return;
        }
        
        // Actualizar solicitud
        try {
            Request request = databaseHelper.getRequestById(editRequestId);
            if (request == null) {
                Toast.makeText(this, "Solicitud no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Actualizar campos
            request.setServiceId(selectedService.getId());
            request.setScheduledDate(dateString);
            request.setScheduledTime(timeString);
            request.setAddress(address);
            request.setNotes(notes);
            request.setTotalPrice(selectedService.getPrice());
            request.setUpdatedAt(databaseHelper.getCurrentDateTime());
            
            boolean result = databaseHelper.updateRequest(request);
            
            if (result) {
                Toast.makeText(this, "Solicitud actualizada exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                errorHandler.handleError(ErrorHandler.ErrorType.DATABASE_ERROR, 
                    "No se pudo actualizar la solicitud en la base de datos");
            }
        } catch (Exception e) {
            errorHandler.handleCriticalError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al actualizar la solicitud", e);
        }
    }
}
