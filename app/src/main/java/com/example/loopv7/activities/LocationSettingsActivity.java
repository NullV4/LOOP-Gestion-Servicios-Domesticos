package com.example.loopv7.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loopv7.R;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.ErrorHandler;
import com.example.loopv7.utils.LocationHelper;
import com.example.loopv7.utils.SessionManager;

/**
 * Actividad para configuración y gestión de ubicación
 * 
 * Funcionalidades:
 * - Solicitar permisos de ubicación en contexto
 * - Obtener ubicación actual del usuario
 * - Configurar ubicación manualmente
 * - Mostrar estado de permisos y ubicación
 * - Integración con perfil de usuario
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class LocationSettingsActivity extends AppCompatActivity implements 
        LocationHelper.LocationCallback, LocationHelper.PermissionCallback {

    private TextView tvLocationStatus, tvCurrentLocation, tvPermissionStatus;
    private Button btnRequestLocation, btnManualLocation, btnOpenSettings;
    private LinearLayout layoutLocationInfo, layoutPermissionInfo;
    
    private LocationHelper locationHelper;
    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;
    private ErrorHandler errorHandler;
    private User currentUser;
    
    private boolean isLocationPermissionGranted = false;
    private boolean isLocationEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_settings);
        
        // Inicializar componentes
        sessionManager = new SessionManager(this);
        databaseHelper = new DatabaseHelper(this);
        errorHandler = ErrorHandler.getInstance(this);
        locationHelper = new LocationHelper(this, this);
        
        // Obtener usuario actual
        currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) {
            errorHandler.handleAuthenticationError("Usuario no encontrado");
            finish();
            return;
        }
        
        initializeViews();
        checkLocationStatus();
        setupListeners();
    }
    
    private void initializeViews() {
        tvLocationStatus = findViewById(R.id.tvLocationStatus);
        tvCurrentLocation = findViewById(R.id.tvCurrentLocation);
        tvPermissionStatus = findViewById(R.id.tvPermissionStatus);
        
        btnRequestLocation = findViewById(R.id.btnRequestLocation);
        btnManualLocation = findViewById(R.id.btnManualLocation);
        btnOpenSettings = findViewById(R.id.btnOpenSettings);
        
        layoutLocationInfo = findViewById(R.id.layoutLocationInfo);
        layoutPermissionInfo = findViewById(R.id.layoutPermissionInfo);
        
        // Configurar callbacks
        locationHelper.setPermissionCallback(this);
    }
    
    private void checkLocationStatus() {
        // Verificar permisos
        isLocationPermissionGranted = locationHelper.checkLocationPermission();
        updatePermissionStatus();
        
        // Verificar si la ubicación está habilitada
        isLocationEnabled = locationHelper.isLocationEnabled();
        updateLocationStatus();
        
        // Mostrar ubicación actual si está disponible
        if (currentUser.getLocation() != null && !currentUser.getLocation().isEmpty()) {
            tvCurrentLocation.setText("Ubicación actual: " + currentUser.getLocation());
        }
    }
    
    private void updatePermissionStatus() {
        if (isLocationPermissionGranted) {
            tvPermissionStatus.setText("✅ Permisos concedidos");
            tvPermissionStatus.setTextColor(getColor(R.color.success));
            layoutPermissionInfo.setVisibility(View.GONE);
        } else {
            tvPermissionStatus.setText("❌ Permisos no concedidos");
            tvPermissionStatus.setTextColor(getColor(R.color.error));
            layoutPermissionInfo.setVisibility(View.VISIBLE);
        }
    }
    
    private void updateLocationStatus() {
        if (isLocationEnabled) {
            tvLocationStatus.setText("✅ Ubicación habilitada");
            tvLocationStatus.setTextColor(getColor(R.color.success));
            btnRequestLocation.setEnabled(true);
        } else {
            tvLocationStatus.setText("❌ Ubicación deshabilitada");
            tvLocationStatus.setTextColor(getColor(R.color.error));
            btnRequestLocation.setEnabled(false);
        }
    }
    
    private void setupListeners() {
        btnRequestLocation.setOnClickListener(v -> requestCurrentLocation());
        btnManualLocation.setOnClickListener(v -> showManualLocationDialog());
        btnOpenSettings.setOnClickListener(v -> openLocationSettings());
    }
    
    private void requestCurrentLocation() {
        if (!isLocationPermissionGranted) {
            showPermissionExplanation();
            return;
        }
        
        if (!isLocationEnabled) {
            showLocationDisabledDialog();
            return;
        }
        
        // Mostrar indicador de carga
        btnRequestLocation.setText("Obteniendo ubicación...");
        btnRequestLocation.setEnabled(false);
        
        // Solicitar ubicación actual
        locationHelper.getCurrentLocation(this);
    }
    
    private void showPermissionExplanation() {
        new AlertDialog.Builder(this)
            .setTitle("Permisos de Ubicación")
            .setMessage("LOOP necesita acceso a tu ubicación para:\n\n" +
                       "• Mostrar servicios cercanos\n" +
                       "• Calcular distancias\n" +
                       "• Mejorar la experiencia de usuario\n\n" +
                       "Solo usamos ubicación aproximada para proteger tu privacidad.")
            .setPositiveButton("Conceder Permisos", (dialog, which) -> {
                locationHelper.requestLocationPermission();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    private void showLocationDisabledDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Ubicación Deshabilitada")
            .setMessage("La ubicación está deshabilitada en tu dispositivo. " +
                       "¿Deseas abrir la configuración para habilitarla?")
            .setPositiveButton("Abrir Configuración", (dialog, which) -> {
                openLocationSettings();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    private void showManualLocationDialog() {
        // Crear diálogo para ingresar ubicación manualmente
        android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Ej: Lima, Perú");
        input.setText(currentUser.getLocation() != null ? currentUser.getLocation() : "");
        
        new AlertDialog.Builder(this)
            .setTitle("Configurar Ubicación Manual")
            .setMessage("Ingresa tu ubicación:")
            .setView(input)
            .setPositiveButton("Guardar", (dialog, which) -> {
                String location = input.getText().toString().trim();
                if (!location.isEmpty()) {
                    saveLocationToProfile(location);
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    private void openLocationSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
    
    private void saveLocationToProfile(String location) {
        try {
            currentUser.setLocation(location);
            
            if (databaseHelper.updateUser(currentUser)) {
                sessionManager.updateCurrentUser(currentUser);
                tvCurrentLocation.setText("Ubicación actual: " + location);
                Toast.makeText(this, "Ubicación guardada exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                errorHandler.handleError(ErrorHandler.ErrorType.DATABASE_ERROR, 
                    "No se pudo guardar la ubicación");
            }
            
        } catch (Exception e) {
            errorHandler.handleCriticalError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al guardar ubicación", e);
        }
    }
    
    // Implementación de LocationHelper.LocationCallback
    @Override
    public void onLocationReceived(Location location) {
        runOnUiThread(() -> {
            btnRequestLocation.setText("Obtener Ubicación Actual");
            btnRequestLocation.setEnabled(true);
            
            if (location != null) {
                // Obtener dirección desde coordenadas
                locationHelper.getAddressFromLocation(location.getLatitude(), 
                    location.getLongitude(), this);
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onLocationError(String error) {
        runOnUiThread(() -> {
            btnRequestLocation.setText("Obtener Ubicación Actual");
            btnRequestLocation.setEnabled(true);
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onAddressReceived(String address) {
        runOnUiThread(() -> {
            saveLocationToProfile(address);
        });
    }
    
    @Override
    public void onAddressError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(this, "Error al obtener dirección: " + error, Toast.LENGTH_SHORT).show();
        });
    }
    
    // Implementación de LocationHelper.PermissionCallback
    @Override
    public void onPermissionGranted() {
        runOnUiThread(() -> {
            isLocationPermissionGranted = true;
            updatePermissionStatus();
            Toast.makeText(this, "Permisos de ubicación concedidos", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onPermissionDenied() {
        runOnUiThread(() -> {
            isLocationPermissionGranted = false;
            updatePermissionStatus();
            Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onPermissionRationaleNeeded() {
        runOnUiThread(() -> {
            showPermissionExplanation();
        });
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Verificar estado de ubicación cuando la actividad se reanuda
        checkLocationStatus();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationHelper != null) {
            locationHelper.cleanup();
        }
    }
}
