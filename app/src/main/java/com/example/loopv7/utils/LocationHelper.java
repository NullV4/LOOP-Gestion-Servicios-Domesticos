package com.example.loopv7.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Clase helper para manejo de ubicación y geolocalización
 * 
 * Funcionalidades:
 * - Solicitud de permisos de ubicación en contexto
 * - Obtención de ubicación actual (aproximada y precisa)
 * - Geocodificación (coordenadas a dirección)
 * - Manejo de errores y estados de ubicación
 * - Callbacks para notificar cambios de ubicación
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class LocationHelper implements LocationListener {
    
    private static final String TAG = "LocationHelper";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final long MIN_TIME_BW_UPDATES = 10000; // 10 segundos
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metros
    
    private Context context;
    private Activity activity;
    private LocationManager locationManager;
    private Geocoder geocoder;
    private ErrorHandler errorHandler;
    
    // Callbacks
    private LocationCallback locationCallback;
    private PermissionCallback permissionCallback;
    
    // Estado de ubicación
    private boolean isLocationEnabled = false;
    private boolean isPermissionGranted = false;
    
    public interface LocationCallback {
        void onLocationReceived(Location location);
        void onLocationError(String error);
        void onAddressReceived(String address);
        void onAddressError(String error);
    }
    
    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
        void onPermissionRationaleNeeded();
    }
    
    public LocationHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.geocoder = new Geocoder(context, Locale.getDefault());
        this.errorHandler = ErrorHandler.getInstance(context);
        
        checkLocationPermission();
    }
    
    /**
     * Verifica si los permisos de ubicación están concedidos
     */
    public boolean checkLocationPermission() {
        boolean coarseLocation = ContextCompat.checkSelfPermission(context, 
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fineLocation = ContextCompat.checkSelfPermission(context, 
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        
        isPermissionGranted = coarseLocation && fineLocation;
        return isPermissionGranted;
    }
    
    /**
     * Solicita permisos de ubicación en contexto
     */
    public void requestLocationPermission() {
        if (checkLocationPermission()) {
            if (permissionCallback != null) {
                permissionCallback.onPermissionGranted();
            }
            return;
        }
        
        // Verificar si necesitamos mostrar explicación
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, 
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (permissionCallback != null) {
                permissionCallback.onPermissionRationaleNeeded();
            }
        }
        
        // Solicitar permisos
        ActivityCompat.requestPermissions(activity,
                new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE);
    }
    
    /**
     * Maneja el resultado de la solicitud de permisos
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true;
                if (permissionCallback != null) {
                    permissionCallback.onPermissionGranted();
                }
            } else {
                isPermissionGranted = false;
                if (permissionCallback != null) {
                    permissionCallback.onPermissionDenied();
                }
            }
        }
    }
    
    /**
     * Verifica si la ubicación está habilitada en el dispositivo
     */
    public boolean isLocationEnabled() {
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        isLocationEnabled = gpsEnabled || networkEnabled;
        return isLocationEnabled;
    }
    
    /**
     * Obtiene la ubicación actual del usuario
     */
    public void getCurrentLocation(LocationCallback callback) {
        this.locationCallback = callback;
        
        if (!checkLocationPermission()) {
            if (callback != null) {
                callback.onLocationError("Permisos de ubicación no concedidos");
            }
            return;
        }
        
        if (!isLocationEnabled()) {
            if (callback != null) {
                callback.onLocationError("Ubicación deshabilitada en el dispositivo");
            }
            return;
        }
        
        try {
            // Intentar obtener ubicación de GPS primero
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
            
            // Intentar obtener ubicación de red como respaldo
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
            
            // Obtener última ubicación conocida
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                if (callback != null) {
                    callback.onLocationReceived(lastKnownLocation);
                }
            }
            
        } catch (SecurityException e) {
            errorHandler.handleError(ErrorHandler.ErrorType.PERMISSION_ERROR, 
                "Error de seguridad al acceder a la ubicación", e);
            if (callback != null) {
                callback.onLocationError("Error de seguridad al acceder a la ubicación");
            }
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al obtener ubicación", e);
            if (callback != null) {
                callback.onLocationError("Error inesperado al obtener ubicación");
            }
        }
    }
    
    /**
     * Obtiene la última ubicación conocida
     */
    private Location getLastKnownLocation() {
        try {
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            
            // Preferir GPS si está disponible y es reciente
            if (gpsLocation != null && isLocationRecent(gpsLocation)) {
                return gpsLocation;
            }
            
            // Usar ubicación de red si está disponible
            if (networkLocation != null && isLocationRecent(networkLocation)) {
                return networkLocation;
            }
            
            // Retornar la mejor disponible
            return gpsLocation != null ? gpsLocation : networkLocation;
            
        } catch (SecurityException e) {
            errorHandler.handleError(ErrorHandler.ErrorType.PERMISSION_ERROR, 
                "Error de seguridad al obtener última ubicación", e);
            return null;
        }
    }
    
    /**
     * Verifica si una ubicación es reciente (menos de 5 minutos)
     */
    private boolean isLocationRecent(Location location) {
        if (location == null) return false;
        
        long timeDiff = System.currentTimeMillis() - location.getTime();
        return timeDiff < 300000; // 5 minutos
    }
    
    /**
     * Convierte coordenadas a dirección usando geocodificación
     */
    public void getAddressFromLocation(double latitude, double longitude, LocationCallback callback) {
        this.locationCallback = callback;
        
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressString = new StringBuilder();
                
                // Construir dirección completa
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    if (i > 0) addressString.append(", ");
                    addressString.append(address.getAddressLine(i));
                }
                
                if (callback != null) {
                    callback.onAddressReceived(addressString.toString());
                }
            } else {
                if (callback != null) {
                    callback.onAddressError("No se pudo encontrar la dirección");
                }
            }
            
        } catch (IOException e) {
            errorHandler.handleError(ErrorHandler.ErrorType.NETWORK_ERROR, 
                "Error de red al obtener dirección", e);
            if (callback != null) {
                callback.onAddressError("Error de red al obtener dirección");
            }
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al obtener dirección", e);
            if (callback != null) {
                callback.onAddressError("Error inesperado al obtener dirección");
            }
        }
    }
    
    /**
     * Convierte dirección a coordenadas usando geocodificación inversa
     */
    public void getLocationFromAddress(String address, LocationCallback callback) {
        this.locationCallback = callback;
        
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            
            if (addresses != null && !addresses.isEmpty()) {
                Address addr = addresses.get(0);
                Location location = new Location("geocoder");
                location.setLatitude(addr.getLatitude());
                location.setLongitude(addr.getLongitude());
                
                if (callback != null) {
                    callback.onLocationReceived(location);
                }
            } else {
                if (callback != null) {
                    callback.onLocationError("No se pudo encontrar la ubicación");
                }
            }
            
        } catch (IOException e) {
            errorHandler.handleError(ErrorHandler.ErrorType.NETWORK_ERROR, 
                "Error de red al obtener coordenadas", e);
            if (callback != null) {
                callback.onLocationError("Error de red al obtener coordenadas");
            }
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al obtener coordenadas", e);
            if (callback != null) {
                callback.onLocationError("Error inesperado al obtener coordenadas");
            }
        }
    }
    
    /**
     * Calcula la distancia entre dos ubicaciones en metros
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        Location location1 = new Location("point1");
        location1.setLatitude(lat1);
        location1.setLongitude(lon1);
        
        Location location2 = new Location("point2");
        location2.setLatitude(lat2);
        location2.setLongitude(lon2);
        
        return location1.distanceTo(location2);
    }
    
    /**
     * Formatea la distancia en una cadena legible
     */
    public static String formatDistance(double distanceInMeters) {
        if (distanceInMeters < 1000) {
            return String.format("%.0f m", distanceInMeters);
        } else {
            return String.format("%.1f km", distanceInMeters / 1000);
        }
    }
    
    /**
     * Detiene las actualizaciones de ubicación
     */
    public void stopLocationUpdates() {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            errorHandler.handleError(ErrorHandler.ErrorType.PERMISSION_ERROR, 
                "Error de seguridad al detener actualizaciones de ubicación", e);
        }
    }
    
    // Implementación de LocationListener
    @Override
    public void onLocationChanged(Location location) {
        if (locationCallback != null) {
            locationCallback.onLocationReceived(location);
        }
        // Detener actualizaciones después de obtener la ubicación
        stopLocationUpdates();
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Status changed: " + provider + " - " + status);
    }
    
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "Provider enabled: " + provider);
    }
    
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "Provider disabled: " + provider);
    }
    
    /**
     * Establece el callback para permisos
     */
    public void setPermissionCallback(PermissionCallback callback) {
        this.permissionCallback = callback;
    }
    
    /**
     * Limpia recursos
     */
    public void cleanup() {
        stopLocationUpdates();
        locationCallback = null;
        permissionCallback = null;
    }
}
