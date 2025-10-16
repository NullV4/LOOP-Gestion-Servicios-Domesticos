package com.example.loopv7.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.loopv7.MainActivity;
import com.example.loopv7.R;
import com.example.loopv7.utils.ErrorHandler;

/**
 * Servicio para tracking de ubicación en tiempo real
 * 
 * Funcionalidades:
 * - Tracking continuo de ubicación durante servicios
 * - Notificaciones de estado de ubicación
 * - Callbacks para actualizaciones de ubicación
 * - Manejo de permisos y errores
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class LocationTrackingService extends Service implements LocationListener {
    
    private static final String TAG = "LocationTrackingService";
    private static final String CHANNEL_ID = "location_tracking_channel";
    private static final int NOTIFICATION_ID = 1002;
    private static final long MIN_TIME_BW_UPDATES = 5000; // 5 segundos
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 5 metros
    
    private LocationManager locationManager;
    private ErrorHandler errorHandler;
    private LocationCallback locationCallback;
    
    // Binder para comunicación con actividades
    private final IBinder binder = new LocationBinder();
    
    public interface LocationCallback {
        void onLocationUpdate(Location location);
        void onLocationError(String error);
    }
    
    public class LocationBinder extends Binder {
        public LocationTrackingService getService() {
            return LocationTrackingService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        errorHandler = ErrorHandler.getInstance(this);
        createNotificationChannel();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        startLocationTracking();
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Seguimiento de Ubicación",
                NotificationManager.IMPORTANCE_LOW
        );
        channel.setDescription("Notificaciones para el seguimiento de ubicación durante servicios");
        
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    
    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("LOOP - Seguimiento de Ubicación")
                .setContentText("Monitoreando tu ubicación durante el servicio")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }
    
    private void startLocationTracking() {
        try {
            // Verificar permisos
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) 
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                errorHandler.handleError(ErrorHandler.ErrorType.PERMISSION_ERROR, 
                    "Permisos de ubicación no concedidos para el servicio");
                stopSelf();
                return;
            }
            
            // Solicitar actualizaciones de ubicación
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                );
            }
            
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                );
            }
            
            Log.i(TAG, "Servicio de tracking de ubicación iniciado");
            
        } catch (SecurityException e) {
            errorHandler.handleError(ErrorHandler.ErrorType.PERMISSION_ERROR, 
                "Error de seguridad al iniciar tracking de ubicación", e);
            stopSelf();
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado al iniciar tracking de ubicación", e);
            stopSelf();
        }
    }
    
    public void setLocationCallback(LocationCallback callback) {
        this.locationCallback = callback;
    }
    
    @Override
    public void onLocationChanged(Location location) {
        if (locationCallback != null) {
            locationCallback.onLocationUpdate(location);
        }
        Log.d(TAG, "Ubicación actualizada: " + location.getLatitude() + ", " + location.getLongitude());
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
        if (locationCallback != null) {
            locationCallback.onLocationError("Proveedor de ubicación deshabilitado: " + provider);
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationTracking();
        Log.i(TAG, "Servicio de tracking de ubicación detenido");
    }
    
    private void stopLocationTracking() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(this);
            }
        } catch (SecurityException e) {
            errorHandler.handleError(ErrorHandler.ErrorType.PERMISSION_ERROR, 
                "Error de seguridad al detener tracking de ubicación", e);
        }
    }
}
