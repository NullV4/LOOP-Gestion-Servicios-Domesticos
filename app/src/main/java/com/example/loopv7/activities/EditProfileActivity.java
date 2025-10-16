package com.example.loopv7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.ErrorHandler;
import com.example.loopv7.utils.SessionManager;
import com.example.loopv7.utils.ValidationHelper;

/**
 * Actividad para editar el perfil del usuario
 * 
 * Funcionalidades:
 * - Editar información básica (nombre, teléfono, descripción, ubicación)
 * - Validación robusta de datos
 * - Manejo de errores centralizado
 * - Actualización en tiempo real
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etPhone, etDescription, etLocation;
    private Button btnSave, btnCancel, btnLocationSettings;
    private TextView tvTitle;
    private ImageView ivProfileImage;
    
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private ValidationHelper validationHelper;
    private ErrorHandler errorHandler;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        // Inicializar componentes
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        validationHelper = new ValidationHelper(this);
        errorHandler = ErrorHandler.getInstance(this);
        
        // Obtener usuario actual
        currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) {
            errorHandler.handleAuthenticationError("Usuario no encontrado");
            finish();
            return;
        }
        
        initializeViews();
        loadUserData();
        setupListeners();
    }
    
    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnLocationSettings = findViewById(R.id.btnLocationSettings);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        
        // Configurar título según el rol
        if (currentUser.isSocia()) {
            tvTitle.setText("Editar Perfil - Socia");
        } else {
            tvTitle.setText("Editar Perfil - Cliente");
        }
    }
    
    private void loadUserData() {
        etName.setText(currentUser.getName());
        etPhone.setText(currentUser.getPhone());
        etDescription.setText(currentUser.getDescription());
        etLocation.setText(currentUser.getLocation());
        
        // Configurar placeholders
        if (currentUser.getDescription() == null || currentUser.getDescription().isEmpty()) {
            etDescription.setHint(currentUser.isSocia() ? 
                "Describe tus servicios y experiencia..." : 
                "Cuéntanos sobre ti...");
        }
        
        if (currentUser.getLocation() == null || currentUser.getLocation().isEmpty()) {
            etLocation.setHint("Ej: Lima, Perú");
        }
    }
    
    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveProfile());
        btnCancel.setOnClickListener(v -> finish());
        btnLocationSettings.setOnClickListener(v -> openLocationSettings());
        
        // Listener para imagen de perfil (placeholder para futura implementación)
        ivProfileImage.setOnClickListener(v -> {
            Toast.makeText(this, "Funcionalidad de imagen de perfil próximamente", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void openLocationSettings() {
        Intent intent = new Intent(this, LocationSettingsActivity.class);
        startActivityForResult(intent, 200);
    }
    
    private void saveProfile() {
        // Obtener datos del formulario
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        
        // Validaciones
        boolean isValid = true;
        
        // Validar nombre
        ValidationHelper.ValidationResult nameResult = validationHelper.validateName(name);
        if (!nameResult.isValid()) {
            etName.setError(nameResult.getMessage());
            etName.requestFocus();
            isValid = false;
        } else {
            etName.setError(null);
        }
        
        // Validar teléfono
        ValidationHelper.ValidationResult phoneResult = validationHelper.validatePhone(phone);
        if (!phoneResult.isValid()) {
            etPhone.setError(phoneResult.getMessage());
            etPhone.requestFocus();
            isValid = false;
        } else {
            etPhone.setError(null);
        }
        
        // Validar descripción (opcional, pero si se proporciona debe tener al menos 10 caracteres)
        if (!description.isEmpty() && description.length() < 10) {
            etDescription.setError("La descripción debe tener al menos 10 caracteres");
            etDescription.requestFocus();
            isValid = false;
        } else if (description.length() > 500) {
            etDescription.setError("La descripción no puede tener más de 500 caracteres");
            etDescription.requestFocus();
            isValid = false;
        } else {
            etDescription.setError(null);
        }
        
        // Validar ubicación (opcional, pero si se proporciona debe tener al menos 3 caracteres)
        if (!location.isEmpty() && location.length() < 3) {
            etLocation.setError("La ubicación debe tener al menos 3 caracteres");
            etLocation.requestFocus();
            isValid = false;
        } else if (location.length() > 100) {
            etLocation.setError("La ubicación no puede tener más de 100 caracteres");
            etLocation.requestFocus();
            isValid = false;
        } else {
            etLocation.setError(null);
        }
        
        if (!isValid) {
            return;
        }
        
        // Actualizar usuario
        try {
            currentUser.setName(name);
            currentUser.setPhone(phone);
            currentUser.setDescription(description.isEmpty() ? null : description);
            currentUser.setLocation(location.isEmpty() ? null : location);
            
            // Actualizar en base de datos
            boolean success = databaseHelper.updateUser(currentUser);
            
            if (success) {
                // Actualizar sesión
                sessionManager.updateCurrentUser(currentUser);
                
                Toast.makeText(this, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show();
                
                // Enviar resultado a la actividad anterior
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profile_updated", true);
                setResult(RESULT_OK, resultIntent);
                
                finish();
            } else {
                errorHandler.handleError(ErrorHandler.ErrorType.DATABASE_ERROR, 
                    "No se pudo actualizar el perfil");
            }
            
        } catch (Exception e) {
            errorHandler.handleCriticalError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al actualizar el perfil", e);
        }
    }
    
    @Override
    public void onBackPressed() {
        // Verificar si hay cambios sin guardar
        if (hasUnsavedChanges()) {
            // Mostrar diálogo de confirmación
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cambios sin guardar")
                .setMessage("¿Deseas salir sin guardar los cambios?")
                .setPositiveButton("Salir", (dialog, which) -> finish())
                .setNegativeButton("Cancelar", null)
                .show();
        } else {
            super.onBackPressed();
        }
    }
    
    private boolean hasUnsavedChanges() {
        String currentName = etName.getText().toString().trim();
        String currentPhone = etPhone.getText().toString().trim();
        String currentDescription = etDescription.getText().toString().trim();
        String currentLocation = etLocation.getText().toString().trim();
        
        return !currentName.equals(currentUser.getName()) ||
               !currentPhone.equals(currentUser.getPhone() != null ? currentUser.getPhone() : "") ||
               !currentDescription.equals(currentUser.getDescription() != null ? currentUser.getDescription() : "") ||
               !currentLocation.equals(currentUser.getLocation() != null ? currentUser.getLocation() : "");
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 200 && resultCode == RESULT_OK) {
            // La ubicación fue actualizada, recargar datos
            loadUserData();
            Toast.makeText(this, "Ubicación actualizada", Toast.LENGTH_SHORT).show();
        }
    }
}
